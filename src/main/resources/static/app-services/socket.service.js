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

        // var colors = [
        //     '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        //     '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
        // ];

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
            const messageElement = document.createElement('li');

            if (message.type === 'JOIN') {
                messageElement.classList.add('event-message');
                message.content = message.sender + ' joined!';
            } else if (message.type === 'LEAVE') {
                messageElement.classList.add('event-message');
                message.content = message.sender + ' left!';
            } else {
                messageElement.classList.add('chat-message');

                var avatarElement = document.createElement('i');
                var avatarText = document.createTextNode(message.sender[0]);
                avatarElement.appendChild(avatarText);
                // avatarElement.style['background-color'] = getAvatarColor(message.sender);

                messageElement.appendChild(avatarElement);

                var usernameElement = document.createElement('span');
                var usernameText = document.createTextNode(message.sender);
                usernameElement.appendChild(usernameText);
                messageElement.appendChild(usernameElement);
            }

            var textElement = document.createElement('p');
            var messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);

            messageElement.appendChild(textElement);

        }


        // function getAvatarColor(messageSender) {
        //     var hash = 0;
        //     for (var i = 0; i < messageSender.length; i++) {
        //         hash = 31 * hash + messageSender.charCodeAt(i);
        //     }
        //     var index = Math.abs(hash % colors.length);
        //     return colors[index];
        // }

    }

})();