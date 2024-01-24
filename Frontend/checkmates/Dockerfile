#################
# Build the app #
#################
FROM node:alpine

WORKDIR /usr/app
COPY  package.json ./
COPY package-lock.json ./
COPY ./ ./
RUN npm install
RUN apk add --no-cache bash


EXPOSE 3000

CMD ["npm",  "start"]