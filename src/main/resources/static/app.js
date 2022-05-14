(function () {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngCookies'])
        .config(config)
        .run(run);

    config.$inject = ['$routeProvider'];

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: 'home/home.view.html',
                controllerAs: 'vm'
            })

            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'login/login.view.html',
                controllerAs: 'vm'
            })

            .when('/register', {
                controller: 'RegisterController',
                templateUrl: 'register/register.view.html',
                controllerAs: 'vm'
            })

            .otherwise({redirectTo: '/login'});
    }

    run.$inject = ['$rootScope', '$http', '$location'];

    function run($rootScope, $http, $location) {
        var currentUser = JSON.parse(localStorage.getItem('currentUser'));
        // keep user logged in after page refresh
        if (currentUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + currentUser.token;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;

            if (restrictedPage && !currentUser) {
                $location.path('/login');
            }
        });
    }

})();