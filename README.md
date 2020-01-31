# FCM Demo

GET /notification – Trigger sample notification with default values sending
curl -H "Content-Type: application/json" -X GET http://localhost:8080/notification


POST /notification/topic – Send a message to a specific topic
curl -d '{"title":"Title:topic", "message":"Send a message to a specific topic", "topic":"common"}' -H "Content-Type: application/json" -X POST http://localhost:8080/notification/topic


POST /notification/token – Send a message to a specific device (with token)
curl -d '{"title":"Title:token", "message":"Test Content...", "token":"d7Nea56ZLGE:APA91bGx57vQ5Z6RsiT-DivJuStYAkSlrBZR9sENnRPwsqM3EDb12-AJMj8P3exM_IsGEPqNX51C-DxsS2Bd7ZsU1YFJW3zjJBGf9flpI-pDRExa9E-eeX3qJFhe9ivr5okz_EYDNM3F", "topic": "common"}' -H "Content-Type: application/json" -X POST http://localhost:8080/notification/token


POST /notification/data – Send a message to a specific topic with additional payload data.
curl -d '{"title":"Title:data", "message":"Send a message to a specific topic with additional payload data.", "topic":"common"}' -H "Content-Type: application/json" -X POST http://localhost:8080/notification/data

POST /notification/data/batch – Send message list to a specific topic.
curl -d '[{"title":"1.Title:topic", "message":"1.Send a message to a specific topic", "topic":"common"},{"title":"2.Title:topic", "message":"2.Send a message to a specific topic", "topic":"common"}]' -H "Content-Type: application/json" -X POST http://localhost:8080/notification/topic/batch
