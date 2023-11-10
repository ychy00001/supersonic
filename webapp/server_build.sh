#!/bin/bash
rm -rf pnpm-lock.yaml pnpm-lock.yaml.back
rm -rf ./packages/supersonic-fe/src/.umi ./packages/supersonic-fe/src/.umi-production
rm -rf node_modules ./packages/supersonic-fe/node_modules  ./packages/chat-sdk/node_modules
cd ./packages/chat-sdk
chown -R 777 /data/project/supersonic/webapp/
pnpm i
chown -R 777 /data/project/supersonic/webapp/
chmod -R g+w /data/project/supersonic/webapp/
pnpm run build --unsafe-perm=true --allow-root
pnpm link --global