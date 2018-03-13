'use strict';

var ErrorState = function($stateProvider) {

    $stateProvider
        .state('error', {
            url: "/error",
            templateUrl: "app/components/error/error.view.html"
        })
};

ErrorState.$inject = ['$stateProvider'];
angular.module('standardUI').config(ErrorState);