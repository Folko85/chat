(function () {
    'use strict';

    angular
        .module('app')
        .factory('MessageService', MessageService);

    MessageService.$inject = ['$http'];

    function MessageService($http) {
        var service = {};

        service.GetMessages = GetMessages;

        return service;

        function GetMessages() {
            return $http.get('/api/messages/all');
        }

    }

})();
