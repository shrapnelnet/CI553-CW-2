FROM alpine:latest
WORKDIR /ci553-cw-2
RUN apk add openjdk21 maven nodejs npm
COPY . ./
RUN npx yarn --cwd web/ && npx yarn --cwd web/ build
RUN mvn install exec:exec
CMD ["mvn", "spring-boot:run"]
EXPOSE 3000