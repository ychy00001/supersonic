package com.tencent.supersonic.common.util.xktime.nlp;

import com.tencent.supersonic.common.util.xktime.constants.Constant;
import com.tencent.supersonic.common.util.xktime.formatter.DateTimeFormatterUtil;
import com.tencent.supersonic.common.util.xktime.utils.CollectionUtil;
import com.tencent.supersonic.common.util.xktime.utils.DateParseUtil;
import com.tencent.supersonic.common.util.xktime.utils.GlobalThreadPool;
import com.tencent.supersonic.common.util.xktime.utils.StringUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;

import static com.tencent.supersonic.common.util.xktime.enums.RegexEnum.NormCurRelatedQuarterBefore;
import static com.tencent.supersonic.common.util.xktime.enums.RegexEnum.NormCurRelatedQuarterNow;
import static com.tencent.supersonic.common.util.xktime.enums.RegexEnum.NormCurRelatedWeekNow;
import static com.tencent.supersonic.common.util.xktime.enums.RegexEnum.NormCurRelatedWeekBefore;

/**
 * 时间自然语言分析工具类<br>
 * 作为xk-time的一个扩展应用功能，主要原理是根据TimeRegex.Gzip正则集合识别时间词语，然后计算得到结果。<br>
 * 
 *  包括功能：  <br>
 *（1）以当前时间为基础分析时间自然语言。  <br>
 *（2）以指定时间为基础分析时间自然语言。 <br>
 *（3）增加多种调用方式，比如parseConcurrent 并发执行，可设置超时时间和自定义线程池等，提高执行效率。<br>
 *
 * 修改自 https://github.com/shinyke/Time-NLP<br>
 * 做了一些修改如下：<br>
 *（1）封装属性，重命名使符合驼峰命名标准。<br>
 *（2）将加载正则资源文件改为单例加载。<br>
 *（3）将类按照功能重新划分为单独的多个类。<br>
 *（4）使用Java8日期API重写。<br>
 *（5）增加注释说明，优化代码。<br>
 *（6）修复原项目中的issue：标准时间yyyy-MM-dd、yyyy-MM-dd HH:mm:ss和yyyy-MM-dd HH:mm解析问题。<br>
 *（7）修复原项目中的issue：1小时后，1个半小时后，1小时50分钟等解析问题；并且支持到秒，比如50秒后，10分钟30秒后等。<br>
 *（8）修复原项目中的issue：修复当前时间是上午10点，那么下午三点 会识别为明天下午三点问题。<br>
 *（9）修复原项目中的issue：修复小数解析异常问题。<br>
 *  <br>
 *
 *注意：NLP会有一定的识别失败率，在不断迭代开发提高成功率
 *
 * @author xkzhangsan
 */
public class TimeNLPUtil {
	
	private TimeNLPUtil(){
	}
	
	/**
	 * 时间自然语言分析
	 * @param text 待分析文本
	 * @return 结果列表
	 */
	public static List<TimeNLP> parse(String text){
		return parse(text, null);
	}
	
	/**
	 * 时间自然语言分析，使用线程池并发执行
	 * 
	 * @param text
	 *            待分析文本
	 * @return 结果列表
	 * @throws InterruptedException 被中止异常
	 * @throws ExecutionException 执行异常
	 * @throws TimeoutException 超时异常
	 */
	public static List<TimeNLP> parseConcurrent(String text)
			throws InterruptedException, ExecutionException, TimeoutException {
		return parse(text, null, 0, null, null);
	}

	/**
	 * 时间自然语言分析，使用线程池并发执行，默认3秒超时
	 *
	 * @param text
	 *            待分析文本
	 * @return 结果列表
	 * @throws InterruptedException 被中止异常
	 * @throws ExecutionException 执行异常
	 * @throws TimeoutException 超时异常
	 */
	public static List<TimeNLP> parseConcurrentDefaultTime(String text)
			throws InterruptedException, ExecutionException, TimeoutException {
		return parse(text, null, Constant.TIME_NLP_TIMEOUT, TimeUnit.MILLISECONDS, null);
	}

	/**
	 * 时间自然语言分析，使用线程池并发执行，设置超时时间和单位
	 *
	 * @param text
	 *            待分析文本
	 * @param timeout
	 *            超时时间
	 * @param unit
	 *            超时时间单位
	 * @return 结果列表
	 * @throws InterruptedException 被中止异常
	 * @throws ExecutionException 执行异常
	 * @throws TimeoutException 超时异常
	 */
	public static List<TimeNLP> parseConcurrent(String text, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return parse(text, null, timeout, unit, null);
	}

	/**
	 * 时间自然语言分析，使用线程池并发执行，设置超时时间和单位，使用自定义线程池
	 *
	 * @param text
	 *            待分析文本
	 * @param timeout
	 *            超时时间
	 * @param unit
	 *            超时时间单位
	 * @param executorService
	 *            自定义线程池
	 * @return 结果列表
	 * @throws InterruptedException 被中止异常
	 * @throws ExecutionException 执行异常
	 * @throws TimeoutException 超时异常
	 */
	public static List<TimeNLP> parseConcurrent(String text, long timeout, TimeUnit unit,
			ExecutorService executorService) throws InterruptedException, ExecutionException, TimeoutException {
		return parse(text, null, timeout, unit, executorService);
	}

	/**
	 * 时间自然语言分析
	 * @param text 待分析文本
	 * @param timeBase 指定时间
	 * @return 结果列表
	 */
	public static List<TimeNLP> parse(String text, String timeBase){
		// 文本预处理
		if(StringUtil.isEmpty(text)){
			return null;
		}
		text = TextPreprocess.preprocess(text);
		if(StringUtil.isEmpty(text)){
			return null;
		}

		//时间名词匹配分析
		List<String> timeStrs = TextAnalysis.getInstance().analysis(text);
		if(CollectionUtil.isEmpty(timeStrs)){
			return null;
		}

		//解析
		List<TimeNLP> timeNLPList = new ArrayList<>(timeStrs.size());
        /**时间上下文： 前一个识别出来的时间会是下一个时间的上下文，用于处理：周六3点到5点这样的多个时间的识别，第二个5点应识别到是周六的。*/
        TimeContext timeContext = new TimeContext();
        if(StringUtil.isEmpty(timeBase)){
        	timeBase = DateTimeFormatterUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
        }
        String oldTimeBase = timeBase;
        timeContext.setTimeBase(timeBase);
        timeContext.setOldTimeBase(oldTimeBase);

        // TODO 临时处理本周 上季度等问题
		List<TimeNLP> tmpList = tmpParseSomeKeyWorld(text, timeContext);
        if(null != tmpList){
        	return tmpList;
		}

        for (int j = 0; j < timeStrs.size(); j++) {
        	if(StringUtil.isEmpty(timeStrs.get(j))){
        		break;
        	}
        	TimeNLP timeNLP = new TimeNLP(timeStrs.get(j), TextAnalysis.getInstance(), timeContext);
        	timeNLPList.add(timeNLP);
            timeContext = timeNLP.getTimeContext();
        }

        /**过滤无法识别的字段*/
        List<TimeNLP> timeNLPListResult = TimeNLP.filterTimeUnit(timeNLPList);
		return timeNLPListResult;
	}

	private static List<TimeNLP> tmpParseSomeKeyWorld(String text, TimeContext timeContext) {
		List<TimeNLP> timeNLPList = null;
		Matcher m0 = NormCurRelatedQuarterNow.getPattern().matcher(text);
		if (m0.find()){
			timeNLPList = new ArrayList<>(2);
			List<LocalDateTime> quarterDateTime = DateParseUtil.getQuarterDateTime(null, 0L);
			TimeNLP start = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(0)), TextAnalysis.getInstance(), timeContext);
			TimeNLP end = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(1)), TextAnalysis.getInstance(), timeContext);
			timeNLPList.add(start);
			timeNLPList.add(end);
			return timeNLPList;
		}
		// TODO 临时处理上季度问题
		Matcher m1 = NormCurRelatedQuarterBefore.getPattern().matcher(text);
		if (m1.find()){
			timeNLPList = new ArrayList<>(2);
			List<LocalDateTime> quarterDateTime = DateParseUtil.getQuarterDateTime(null, -1L);
			TimeNLP start = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(0)), TextAnalysis.getInstance(), timeContext);
			TimeNLP end = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(1)), TextAnalysis.getInstance(), timeContext);
			timeNLPList.add(start);
			timeNLPList.add(end);
			return timeNLPList;
		}
		// TODO 临时处理本周问题
		Matcher m2 = NormCurRelatedWeekNow.getPattern().matcher(text);
		if (m2.find()){
			timeNLPList = new ArrayList<>(2);
			List<LocalDateTime> quarterDateTime = DateParseUtil.getWeekDateTime(null, 0);
			TimeNLP start = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(0)), TextAnalysis.getInstance(), timeContext);
			TimeNLP end = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(1)), TextAnalysis.getInstance(), timeContext);
			timeNLPList.add(start);
			timeNLPList.add(end);
			return timeNLPList;
		}
		// TODO 临时处理上周问题
		Matcher m3 = NormCurRelatedWeekBefore.getPattern().matcher(text);
		if (m3.find()){
			timeNLPList = new ArrayList<>(2);
			List<LocalDateTime> quarterDateTime = DateParseUtil.getWeekDateTime(null, -1);
			TimeNLP start = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(0)), TextAnalysis.getInstance(), timeContext);
			TimeNLP end = new TimeNLP(DateTimeFormatterUtil.formatToDateTimeStr(quarterDateTime.get(1)), TextAnalysis.getInstance(), timeContext);
			timeNLPList.add(start);
			timeNLPList.add(end);
			return timeNLPList;
		}
		// TODO 第一季度 1季度  第二季度 2季度 .....

		return timeNLPList;
	}

	/**
	 * 时间自然语言分析，使用线程池并发执行
	 *
	 * @param text
	 *            待分析文本
	 * @param timeBase
	 *            指定时间
	 * @param timeout
	 *            超时时间
	 * @param unit
	 *            超时时间单位
	 * @param executorService
	 *            自定义线程池
	 * @return 结果列表
	 * @throws InterruptedException 被中止异常
	 * @throws ExecutionException 执行异常
	 * @throws TimeoutException 超时异常
	 */
	public static List<TimeNLP> parse(String text, String timeBase, long timeout, TimeUnit unit,
			ExecutorService executorService) throws InterruptedException, ExecutionException, TimeoutException {
		List<TimeNLP> result = null;
		if (StringUtil.isEmpty(text)) {
			return result;
		}

		if (timeout > 0) {
			if (executorService != null) {
				result = executorService.submit(new TimeNLPCallable(text, timeBase)).get(timeout, unit);
			} else {
				result = GlobalThreadPool.getGlobalThreadPool().submit(new TimeNLPCallable(text, timeBase)).get(timeout,
						unit);
			}
		} else {
			if (executorService != null) {
				result = executorService.submit(new TimeNLPCallable(text, timeBase)).get();
			} else {
				result = GlobalThreadPool.getGlobalThreadPool().submit(new TimeNLPCallable(text, timeBase)).get();
			}
		}
		return result;
	}
	
}
