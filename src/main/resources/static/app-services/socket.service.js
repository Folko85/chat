(function () {
    'use strict';

    angular
        .module('app')
        .factory('SocketService', SocketService);

    SocketService.$inject = ['FlashService'];

    function SocketService(FlashService) {
        var service = {};

        service.connect = connect;
        service.onConnected = onConnected;
        service.onError = onError;
        service.sendMessage = sendMessage;
        service.onMessageReceived = onMessageReceived;

        return service;

        var stompClient = null;
        var username = null;

        function connect(currentUser) {
            if (currentUser.username) {
                username = currentUser.username;
                const socket = new SockJS('/ws');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, onConnected, onError);
            }
        }


        function onConnected() {
            // Subscribe to the Public Topic
            stompClient.subscribe('/topic/public', onMessageReceived);

            // Tell your username to the server
            stompClient.send("/app/chat.addUser",
                {},
                JSON.stringify({sender: username, type: 'JOIN'})
            )
        }


        function onError() {
            FlashService.Error('Не вышло подключиться. Обновите страницу и попробуйте снова!');
        }


        function sendMessage(messageContent) {
            $('.new-message').val('');
            if (messageContent && stompClient) {
                const chatMessage = {
                    sender: username,
                    dateTime: new Date(),
                    content: messageContent,
                    type: 'CHAT'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            }
        }


        function onMessageReceived(payload) {
            const message = JSON.parse(payload.body);
            addMessage(message);
        }

        function getMessageElement(message) {
            let item = $('<div class="message-item"></div>');
            let header = $('<div class="message-header"></div>');
            header.append($('<div class="datetime">' +
                message.dateTime + '</div>'));
            header.append($('<div class="username">' +
                message.sender + '</div>'));
            let textElement = $('<div class="message-text"></div>');
            textElement.text(message.content);
            item.append(header, textElement);
            return item;
        }

        function addMessage(message) {
            if (message.content) {
                const element = getMessageElement(message);
                $('.messages-list').append(element);
                const block = document.getElementById("messages");
                block.scrollTop = block.scrollHeight;
            } else if (message.type === "JOIN") {
                let textElement = $('<div class="username"></div>');
                textElement.text("К нам присоединился " + message.sender);
                $('.messages-list').append(textElement);
                const block = document.getElementById("messages");
                block.scrollTop = block.scrollHeight;
            } else if (message.type === "LEAVE"){
                let textElement = $('<div class="username"></div>');
                textElement.text("Нас покинул " + message.sender);
                $('.messages-list').append(textElement);
                const block = document.getElementById("messages");
                block.scrollTop = block.scrollHeight;
            }
        }

    }

})();