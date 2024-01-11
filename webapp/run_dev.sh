cd ./packages/supersonic-fe
pnpm link ../chat-sdk
pnpm i
export NODE_OPTIONS="--max-old-space-size=4096"
pnpm start