package com.tencent.supersonic.common.util.xktime.enums;

import com.tencent.supersonic.common.util.xktime.utils.RegexCache;

import java.util.regex.Pattern;


/**
 * 正则枚举
 * 
 * @author xkzhangsan
 */
public enum RegexEnum {
		
	//================================nlp normStandardTime================================
	/**
	 *  标准时间
	 * <pre>
	 *     yyyy-MM-dd HH:mm:ss
	 *     yyyy-MM-dd HH:mm
	 *     yyyy-MM-dd
	 * </pre>
	 *  <p>
	 *  正则： "\\d{4}-\\d{1,2}-\\d{1,2}( \\d{1,2}:\\d{1,2}(:\\d{1,2})?)?"
	 */
	NormStandard("\\d{4}-\\d{1,2}-\\d{1,2}( \\d{1,2}:\\d{1,2}(:\\d{1,2})?)?", 0, "标准时间"),
	
	/**
	 *  标准时间中文
	 * <pre>
	 *     yyyy年MM月dd日 HH:mm:ss
	 *     yyyy年MM月dd日 HH:mm
	 *     yyyy年MM月dd日
	 * </pre>
	 *  <p>
	 *  正则： "\\d{4}(年)\\d{1,2}(月)\\d{1,2}(日)( \\d{1,2}:\\d{1,2}(:\\d{1,2})?)?"
	 */
	NormStandardCn("\\d{4}(年)\\d{1,2}(月)\\d{1,2}(日)( \\d{1,2}:\\d{1,2}(:\\d{1,2})?)?", 0, "标准时间中文"),	
	
	
	
	//================================nlp normYear================================
	/**
	 *  两位数来表示年份
	 *  <p>
	 *  正则： "[0-9]{2}(?=年)"
	 */
	NormYearTwo("[0-9]{2}(?=年)", 0, "两位数来表示年份"),
	/**
	 *  三位数和四位数表示的年份
	 *  <p>
	 *  正则： "[0-9]?[0-9]{3}(?=年)"
	 */
	NormYearFour("[0-9]?[0-9]{3}(?=年)", 0, "三位数和四位数表示的年份"),
	
	//================================nlp normMonth================================
	/**
	 *  月字段
	 *  <p>
	 *  正则： "((10)|(11)|(12)|([1-9]))(?=月)"
	 */
	NormMonth("((10)|(11)|(12)|([1-9]))(?=月)", 0, "月字段"),
	
	
	//================================nlp normDay================================
	/**
	 *  日字段
	 *  <p>
	 *  正则：{@code  "((?<!\\d))([0-3][0-9]|[1-9])(?=(日|号))" }
	 *  
	 */
	NormDay("((?<!\\d))([0-3][0-9]|[1-9])(?=(日|号))", 0, "日字段"),
	
	
	
	//================================nlp normMonthFuzzyDay================================
	/**
	 *  月-日 兼容模糊写法
	 *  <p>
	 *  正则： "((10)|(11)|(12)|([1-9]))(月|\\.|\\-)([0-3][0-9]|[1-9])"
	 */
	NormMonthFuzzyDay("((10)|(11)|(12)|([1-9]))(月|\\.|\\-)([0-3][0-9]|[1-9])", 0, "月-日 兼容模糊写法"),
	
	/**
	 *  月-日 兼容模糊写法分隔符
	 *  <p>
	 *  正则： "(月|\\.|\\-)"  
	 */
	NormMonthFuzzyDaySeparator("(月|\\.|\\-)", 0, "月-日 兼容模糊写法分隔符"),
	
	
	//================================nlp normBaseRelated================================
	/**
	 *  几天前
	 *  <p>
	 *  正则：{@code  "\\d+(?=天[以之]?前)" }
	 */
	NormBaseRelatedDayBefore("\\d+(?=天[以之]?前)", 0, "几天前"),
	
	
	/**
	 *  几天后
	 *  <p>
	 *  正则：{@code  "\\d+(?=天[以之]?后)" }
	 */
	NormBaseRelatedDayAfter("\\d+(?=天[以之]?后)", 0, "几天后"),
	
	
	/**
	 *  几月前
	 *  <p>
	 *  正则：{@code  "\\d+(?=(个)?月[以之]?前)" }
	 */
	NormBaseRelatedMonthBefore("\\d+(?=(个)?月[以之]?前)", 0, "几月前"),
	
	
	/**
	 *  几月后
	 *  <p>
	 *  正则：{@code  "\\d+(?=(个)?月[以之]?后)" }
	 */
	NormBaseRelatedMonthAfter("\\d+(?=(个)?月[以之]?后)", 0, "几月后"),
	

	/**
	 *  几年前
	 *  <p>
	 *  正则：{@code  "\\d+(?=年[以之]?前)" }
	 */
	NormBaseRelatedYearBefore("\\d+(?=年[以之]?前)", 0, "几年前"),

	/**
	 *  过去n年
	 *  <p>
	 *  正则：{@code  "过去\d+年内?" }
	 */
	NormBaseRelatedYearBeforeRange("(?<=过去)\\d+(?=年)", 0, "过去n年内"),


	/**
	 *  几年后
	 *  <p>
	 *  正则：{@code  "\\d+(?=年[以之]?后)" }
	 */
	NormBaseRelatedYearAfter("\\d+(?=年[以之]?后)", 0, "几年后"),
	
	
	//================================nlp normBaseTimeRelated================================
	
	/**
	 *  3小时前
	 *  <p>
	 *  正则：{@code  "\\d+(?=个?半?(小时|钟头|h|H)[以之]?前)" }
	 */
	NormBaseTimeRelatedHourBefore("\\d+(?=个?半?(小时|钟头|h|H)[以之]?前)", 0, "3小时前"),
	
	
	/**
	 *  3小时后
	 *  <p>
	 *  正则：{@code  "\\d+(?=个?半?(小时|钟头|h|H)[以之]?后)" }
	 */
	NormBaseTimeRelatedHourAfter("\\d+(?=个?半?(小时|钟头|h|H)[以之]?后)", 0, "3小时后"),
	
	
	/**
	 *  半个小时前
	 *  <p>
	 *  正则：{@code  "半个?(小时|钟头)[以之]?前" }
	 */
	NormBaseTimeRelatedHalfHourBefore("半个?(小时|钟头)[以之]?前", 0, "半个小时前"),
	
	
	/**
	 *  半个小时后
	 *  <p>
	 *  正则：{@code  "半个?(小时|钟头)[以之]?后" }
	 */
	NormBaseTimeRelatedHalfHourAfter("半个?(小时|钟头)[以之]?后", 0, "半个小时后"),
	
	
	/**
	 *  1个小时
	 *  <p>
	 *  正则：{@code  "\\d+(?=个?半?(小时|钟头|h|H))" }
	 */
	NormBaseTimeRelatedHour("\\d+(?=个?半?(小时|钟头|h|H))", 0, "1个小时"),
	
	/**
	 *  3分钟前
	 *  <p>
	 *  正则：{@code  "\\d+(?=(分钟|分|min)[以之]?前)" }
	 */
	NormBaseTimeRelatedMinuteBefore("\\d+(?=(分钟|分|min)[以之]?前)", 0, "3分钟前"),
	
	
	/**
	 *  3分钟后
	 *  <p>
	 *  正则：{@code  "\\d+(?=(分钟|分|min)[以之]?后)" }
	 */
	NormBaseTimeRelatedMinuteAfter("\\d+(?=(分钟|分|min)[以之]?后)", 0, "3分钟后"),
	
	/**
	 *  1分钟
	 *  <p>
	 *  正则：{@code  "\\d+(?=(分钟|min))" }
	 */
	NormBaseTimeRelatedMinute("\\d+(?=(分钟|min))", 0, "1分钟"),
	
	
	/**
	 *  3秒钟前
	 *  <p>
	 *  正则：{@code  "\\d+(?=(秒钟|秒|sec)[以之]?前)" }
	 */
	NormBaseTimeRelatedSecondBefore("\\d+(?=(秒钟|秒|sec)[以之]?前)", 0, "3秒钟前"),
	
	
	/**
	 *  3秒钟后
	 *  <p>
	 *  正则：{@code  "\\d+(?=(秒钟|秒|sec)[以之]?后)" }
	 */
	NormBaseTimeRelatedSecondAfter("\\d+(?=(秒钟|秒|sec)[以之]?后)", 0, "3秒钟后"),
	
	
	//================================nlp normCurRelated================================
	
	/**
	 *  前年
	 *  <p>
	 *  正则：{@code  "前年" }
	 */
	NormCurRelatedYearBeforeLast("前年", 0, "前年"),
	
	/**
	 *  去年
	 *  <p>
	 *  正则：{@code  "去年" }
	 */
	NormCurRelatedYearBefore("去年", 0, "去年"),
	
	/**
	 *  今年
	 *  <p>
	 *  正则：{@code  "今年" }
	 */
	NormCurRelatedYear("今年", 0, "今年"),
	
	/**
	 *  明年
	 *  <p>
	 *  正则：{@code  "明年" }
	 */
	NormCurRelatedYearAfter("明年", 0, "明年"),
	
	/**
	 *  后年
	 *  <p>
	 *  正则：{@code  "后年" }
	 */
	NormCurRelatedYearAfterNext("后年", 0, "后年"),
	
	/**
	 *  上个月
	 *  <p>
	 *  正则：{@code  "上(个)?月" }
	 */
	NormCurRelatedMonthBefore("上(个)?月", 0, "上个月"),

	/**
	 *  上季度
	 *  <p>
	 *  正则：{@code  "上(个)?季度" }
	 */
	NormCurRelatedQuarterBefore("上(一|个)?季度", 0, "上个季度"),

	/**
	 *  本季度
	 *  <p>
	 *  正则：{@code  "本(个)?季度" }
	 */
	NormCurRelatedQuarterNow("(本|当前|这)季度", 0, "本季度"),

	/**
	 *  第几季度
	 *  <p>
	 *  正则：{@code  "(?<=第)[一二三四1234](?=季度)" }
	 */
	NormCurRelatedQuarterWithYear("(?<=第)[一二三四1234](?=季度)", 0, "第N季度"),

	/**
	 *  季度下第几年
	 *  <p>
	 *  正则：{@code  ".*?(?=第)" }
	 */
	NormCurRelatedQuarterYear(".*?(?=第)", 0, "第N季度的年"),

	/**
	 *  这个月
	 *  <p>
	 *  正则：{@code  "(本|这个)月" }
	 */
	NormCurRelatedMonth("(本|这个)月", 0, "这个月"),
	
	/**
	 *  下个月
	 *  <p>
	 *  正则：{@code  "下(个)?月" }
	 */
	NormCurRelatedMonthAfter("下(个)?月", 0, "下个月"),
	
	/**
	 *  大前天
	 *  <p>
	 *  正则：{@code  "大前天" }
	 */
	NormCurRelatedDayBeforeThree("大前天", 0, "大前天"),
	
	/**
	 *  前天
	 *  <p>
	 *  正则：{@code  "(?<!大)前天" }
	 */
	NormCurRelatedDayBeforeLast("(?<!大)前天", 0, "前天"),
	
	
	/**
	 *  昨天
	 *  <p>
	 *  正则：{@code  "昨" }
	 */
	NormCurRelatedDayYesterday("昨", 0, "昨天"),
	/**
	 *  今天
	 *  <p>
	 *  正则：{@code  "今(?!年)" }
	 */
	NormCurRelatedDayToday("今(?!年)", 0, "今天"),
	
	/**
	 *  明天
	 *  <p>
	 *  正则：{@code  "明(?!年)" }
	 */
	NormCurRelatedDayTomorrow("明(?!年)", 0, "明天"),
	
	/**
	 *  后天
	 *  <p>
	 *  正则：{@code  "(?<!大)后天" }
	 */
	NormCurRelatedDayAfterNext("(?<!大)后天", 0, "后天"),
	
	
	/**
	 *  大后天
	 *  <p>
	 *  正则：{@code  "大后天" }
	 */
	NormCurRelatedDayAfterThree("大后天", 0, "大后天"),

	/**
	 *  本周
	 *  <p>
	 *  正则：{@code  "(?<=((?<!上)上(周|星期)))[1-7]?" }
	 */
	NormCurRelatedWeekNow("(?<=((本|这|当前)(周|星期)))[1-7]?", 0, "本周"),


	/**
	 *  上上周
	 *  <p>
	 *  正则：{@code  "(?<=(上上(周|星期)))[1-7]?" }
	 */
	NormCurRelatedWeekBeforeLast("(?<=(上上(周|星期)))[1-7]?", 0, "上上周"),
	
	/**
	 *  上周
	 *  <p>
	 *  正则：{@code  "(?<=((?<!上)上(周|星期)))[1-7]?" }
	 */
	NormCurRelatedWeekBefore("(?<=((?<!上)上(周|星期)))[1-7]?", 0, "上周"),

	/**
	 *  近两周
	 *  <p>
	 *  正则：{@code  "近(两|2)(周|星期)" }
	 */
	NormCurRelatedWeekBeforeTwo("近(两|2)(周|星期)", 0, "近两周"),

	/**
	 *  近三周
	 *  <p>
	 *  正则：{@code  "近(三|3)(周|星期)" }
	 */
	NormCurRelatedWeekBeforeThree("近(三|3)(周|星期)", 0, "近两周"),


	/**
	 * 周一 二等
	 *  <p>
	 *  正则：{@code  "(?<=((?<!(上|下))(周|星期)))[1-7]?" }
	 */
	NormCurRelatedWeek("(?<=((?<!(上|下))(周|星期)))[1-7]?", 0, "周一 二等"),
	
	/**
	 *  下周
	 *  <p>
	 *  正则：{@code  "(?<=((?<!下)下(周|星期)))[1-7]?" }
	 */
	NormCurRelatedWeekAfter("(?<=((?<!下)下(周|星期)))[1-7]?", 0, "下周"),
	
	/**
	 * 下下周
	 *  <p>
	 *  正则：{@code  "(?<=(下下(周|星期)))[1-7]?" }
	 */
	NormCurRelatedWeekAfterNext("(?<=(下下(周|星期)))[1-7]?", 0, "下下周"),	
	
	//================================nlp normHour================================
	/**
	 *  时字段
	 *  <p>
	 *  正则：{@code  "(?<!(周|星期))([0-2]?[0-9])(?=(点|时))" }
	 */
	NormHour("(?<!(周|星期))([0-2]?[0-9])(?=(点|时))", 0, "时字段"),
	
	/**
	 *  凌晨
	 *  <p>
	 *  正则：{@code  "凌晨" }
	 */
	NormHourDayBreak("凌晨", 0, "凌晨"),
	
	/**
	 *  早上
	 *  <p>
	 *  正则：{@code  "早上|早晨|早间|晨间|今早|明早" }  
	 */
	NormHourEarlyMorning("早上|早晨|早间|晨间|今早|明早", 0, "早上"),
	
	
	/**
	 *  上午
	 *  <p>
	 *  正则：{@code  "上午" }
	 */
	NormHourMorning("上午", 0, "上午"),
	
	/**
	 *  中午
	 *  <p>
	 *  正则：{@code  "(中午)|(午间)" }
	 */
	NormHourNoon("(中午)|(午间)", 0, "中午"),
	
	/**
	 *  下午
	 *  <p>
	 *  正则：{@code  "(下午)|(午后)|(pm)|(PM)" }
	 */
	NormHourAfternoon("(下午)|(午后)|(pm)|(PM)", 0, "下午"),
	
	/**
	 *  晚上
	 *  <p>
	 *  正则：{@code  "晚上|夜间|夜里|今晚|明晚" }
	 */
	NormHourNight("晚上|夜间|夜里|今晚|明晚", 0, "晚上"),
	
	
	//================================nlp normMinute================================	
	/**
	 *  分字段
	 *  <p>
	 *  正则：{@code  "([0-5]?[0-9](?=分(?!钟)))|((?<=((?<!小)[点时]))[0-5]?[0-9](?!刻))" }
	 */
	NormMinute("([0-5]?[0-9](?=分(?!钟)))|((?<=((?<!小)[点时]))[0-5]?[0-9](?!刻))", 0, "分字段"),
	
	
	/**
	 *  排除30分后
	 *  <p>
	 *  正则：{@code  "(\\d+(分钟|分|min)[以之]?[前后])" }
	 */
	NormMinuteSpec("(\\d+(分钟|分|min)[以之]?[前后])", 0, "排除30分后"),
	
	/**
	 *  一刻
	 *  <p>
	 *  正则：{@code  "(?<=[点时])[1一]刻(?!钟)" }
	 */
	NormMinuteOneQuarter("(?<=[点时])[1一]刻(?!钟)", 0, "一刻"),
	
	/**
	 *  半
	 *  <p>
	 *  正则：{@code  "(?<=[点时])半" }
	 */
	NormMinuteHalf("(?<=[点时])半", 0, "点半"),
	
	/**
	 *  3刻
	 *  <p>
	 *  正则：{@code  "(?<=[点时])[3三]刻(?!钟)" }
	 */
	NormMinuteThreeQuarter("(?<=[点时])[3三]刻(?!钟)", 0, "3刻"),
	
	
	//================================nlp normSecond================================
	
	/**
	 *  排除30秒后
	 *  <p>
	 *  正则：{@code  "(\\d+(秒钟|秒|sec)[以之]?[前后])" }
	 */
	NormSecondSpec("(\\d+(秒钟|秒|sec)[以之]?[前后])", 0, "排除30秒后"),
	
	/**
	 *  秒字段
	 *  <p>
	 *  正则：{@code  "([0-5]?[0-9](?=秒))|((?<=分)[0-5]?[0-9])" }
	 */
	NormSecond("([0-5]?[0-9](?=秒))|((?<=分)[0-5]?[0-9])", 0, "秒字段"),
	
	
	//================================nlp normTotal================================
	
	
	/**
	 *  时分秒
	 *  <p>
	 *  正则：{@code  "(?<!(周|星期))([0-2]?[0-9]):[0-5]?[0-9]:[0-5]?[0-9]" }
	 */
	NormTotalTime("(?<!(周|星期))([0-2]?[0-9]):[0-5]?[0-9]:[0-5]?[0-9]", 0, "时分秒"),
	
	/**
	 *  时分
	 *  <p>
	 *  正则：{@code  "(?<!(周|星期))([0-2]?[0-9]):[0-5]?[0-9]" }
	 */
	NormTotalTimeShort("(?<!(周|星期))([0-2]?[0-9]):[0-5]?[0-9]", 0, "时分"),
	
	/**
	 *  晚上
	 *  <p>
	 *  正则：{@code  "晚" }
	 */
	NormTotalNight("晚", 0, "晚上"),
	
	/**
	 *  年月日 -
	 *  <p>
	 *  正则：{@code  "[0-9]?[0-9]?[0-9]{2}-((10)|(11)|(12)|([1-9]))-((?<!\\d))([0-3][0-9]|[1-9])" }
	 */
	NormTotalDateOne("[0-9]?[0-9]?[0-9]{2}-((10)|(11)|(12)|([1-9]))-((?<!\\d))([0-3][0-9]|[1-9])", 0, "年月日 -"),
	
	/**
	 *  年月日 /
	 *  <p>
	 *  正则：{@code  "((10)|(11)|(12)|([1-9]))/((?<!\\d))([0-3][0-9]|[1-9])/[0-9]?[0-9]?[0-9]{2}" }
	 */
	NormTotalDateTwo("((10)|(11)|(12)|([1-9]))/((?<!\\d))([0-3][0-9]|[1-9])/[0-9]?[0-9]?[0-9]{2}", 0, "年月日 /"),
	
	/**
	 *  年月日 .
	 *  <p>
	 *  正则：{@code  "[0-9]?[0-9]?[0-9]{2}\\.((10)|(11)|(12)|([1-9]))\\.((?<!\\d))([0-3][0-9]|[1-9])" }
	 */
	NormTotalDateThree("[0-9]?[0-9]?[0-9]{2}\\.((10)|(11)|(12)|([1-9]))\\.((?<!\\d))([0-3][0-9]|[1-9])", 0, "年月日 ."),
	
	
	//================================nlp TextPreprocess================================	
	/**
	 *  的
	 *  <p>
	 *  正则：{@code  "[的]+" }
	 */
	TextPreprocessSeparator("[的]+", 0, "凌晨"),
	
	/**
	 *  数字正则1
	 *  <p>
	 *  正则：{@code  "[一二两三四五六七八九123456789]万[一二两三四五六七八九123456789](?!(千|百|十))" }
	 */
	TextPreprocessNumberTranslatorOne("[一二两三四五六七八九123456789]万[一二两三四五六七八九123456789](?!(千|百|十))", 0, "数字正则1"),
	
	/**
	 *  数字正则2
	 *  <p>
	 *  正则：{@code  "[一二两三四五六七八九123456789]千[一二两三四五六七八九123456789](?!(百|十))" }
	 */
	TextPreprocessNumberTranslatorTwo("[一二两三四五六七八九123456789]千[一二两三四五六七八九123456789](?!(百|十))", 0, "数字正则2"),
	
	/**
	 *  数字正则3
	 *  <p>
	 *  正则：{@code  "[一二两三四五六七八九123456789]百[一二两三四五六七八九123456789](?!十)" }
	 */
	TextPreprocessNumberTranslatorThree("[一二两三四五六七八九123456789]百[一二两三四五六七八九123456789](?!十)", 0, "数字正则3"),
	
	/**
	 *  数字正则4
	 *  <p>
	 *  正则：{@code  "[零一二两三四五六七八九]" }
	 */
	TextPreprocessNumberTranslatorFour("[零一二两三四五六七八九]", 0, "数字正则4"),
	
	/**
	 *  数字正则5
	 *  <p>
	 *  正则：{@code  "(?<=(周|星期))[末天日]" }
	 */
	TextPreprocessNumberTranslatorFive("(?<=(周|星期))[末天日]", 0, "数字正则5"),
	
	/**
	 *  数字正则6
	 *  <p>
	 *  正则：{@code  "(?<!(周|星期))0?[0-9]?十[0-9]?" }
	 */
	TextPreprocessNumberTranslatorSix("(?<!(周|星期))0?[0-9]?十[0-9]?", 0, "数字正则6"),
	
	/**
	 *  数字正则7
	 *  <p>
	 *  正则：{@code  "0?[1-9]百[0-9]?[0-9]?" }
	 */
	TextPreprocessNumberTranslatorSeven("0?[1-9]百[0-9]?[0-9]?", 0, "数字正则7"),
	
	/**
	 *  数字正则8
	 *  <p>
	 *  正则：{@code  "0?[1-9]千[0-9]?[0-9]?[0-9]?" }
	 */
	TextPreprocessNumberTranslatorEight("0?[1-9]千[0-9]?[0-9]?[0-9]?", 0, "数字正则8"),
	
	/**
	 *  数字正则9
	 *  <p>
	 *  正则：{@code  "[0-9]+万[0-9]?[0-9]?[0-9]?[0-9]?" }
	 */
	TextPreprocessNumberTranslatorNine("[0-9]+万[0-9]?[0-9]?[0-9]?[0-9]?", 0, "数字正则9"),	
	
	/**
	 *  日号
	 *  <p>
	 *  正则：{@code  "[日号]" }
	 */
	TextPreprocessDelDecimalStrSeparator("[日号]", 0, "日号"),
	
	/**
	 *  小数
	 *  <p>
	 *  正则：{@code  "{0,1}\\d+\\.\\d*|{0,1}\\d*\\.\\d+" }
	 */
	TextPreprocessDelDecimalStr("{0,1}\\d+\\.\\d*|{0,1}\\d*\\.\\d+", 0, "小数"),	
	;
	
	
	private String rule;
	
	private int flags;
	
	private String desc;
	
	private RegexEnum(String rule, int flags, String desc) {
		this.rule = rule;
		this.flags = flags;
		this.desc = desc;
	}
	
	/**
	 * 先从缓存中查找正则，没有编译后放入缓存
	 * 
	 * @return Pattern
	 */
	public Pattern getPattern(){
		return RegexCache.get(this.rule, this.flags);
	}

	public String getName() {
		return this.name();
	}

	public String getRule() {
		return rule;
	}

	public int getFlags() {
		return flags;
	}

	public String getDesc() {
		return desc;
	}

}
