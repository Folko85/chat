(function () {
    'use strict';

    angular
        .module('app')
        .factory('MessageService', MessageService);

    MessageService.$inject = ['$http'];

    function MessageService($http) {
        var service = {};

        service.GetMessages = GetMessages;
        service.AddMessages = AddMessages;

        return service;

        function GetMessages() {
            return $http.get('/api/messages/all');
        }

        function AddMessages(message) {
            return $http.post('/api/messages/add', {message: message});
        }

    }

})();
