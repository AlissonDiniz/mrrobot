/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module("mrrobot", []);
app.service("MainService", MainService);
app.controller("MainController", MainController);

MainController.$inject = ['$scope', 'MainService', '$timeout'];
function MainController($scope, MainService, $timeout) {

    $scope.classification = {text: '', category: '', inactive: false};
    $scope.categoryModel = {name: ''};
    $scope.categoryList = {onload: true, data: []};
    $scope.response = {show: false};
    $scope.request = {progress: false};
    $scope.modal = $('div#category-modal');

    $scope.classify = function () {
        $scope.request.progress = true;
        $scope.classification.inactive = true;
        var promisse = MainService.performClassify($scope.classification.text);
        promisse.then(function (response) {
            $scope.response.show = true;
            $scope.request.progress = false;
            $scope.classification.category = response.data.name;
        });
    };

    $scope.categoryCorrect = function () {
        $scope.classification.text = '';
        $scope.classification.category = '';
        $scope.response.show = false;
        $scope.classification.inactive = false;
    };

    $scope.categoryWrong = function () {
        $scope.classification.inactive = false;
        $scope.response.show = false;
        $scope.modal.modal();
    };

    $scope.updateCategoryList = function () {
        var promisse = MainService.loadCategories();
        promisse.then(function (response) {
            $scope.categoryList.data = response.data;
            $scope.categoryList.onload = false;
        });
    };

    $scope.applyCategory = function (category) {
        var promisse = MainService.applyCategory(category, $scope.classification.text);
        promisse.then(function (response) {
            $scope.classification.text = '';
            $scope.classification.category = '';
            $scope.response.show = false;
            $scope.classification.inactive = false;
            $scope.modal.modal('hide');
        });
    };

    $scope.createCategory = function () {
        var promisse = MainService.createCategory($scope.categoryModel, $scope.classification.text);
        promisse.then(function () {
            $scope.updateCategoryList();
            $scope.classification.text = '';
            $scope.classification.category = '';
            $scope.categoryModel.name = '';
            $scope.response.show = false;
            $scope.classification.inactive = false;
            $scope.modal.modal('hide');
        });
    };

    (function init() {
        $scope.updateCategoryList();
    })();

}


MainService.$inject = ['$http'];
function MainService($http) {
    var service = this;

    service.loadCategories = function () {
        return $http({
            method: 'GET',
            url: '/category/list'
        });
    };

    service.performClassify = function (text) {
        return $http({
            method: 'POST',
            url: '/textClassification/classify',
            dataType: 'json',
            data: {text: text}
        });
    };

    service.applyCategory = function (category, text) {
        return $http({
            method: 'POST',
            url: '/textClassification/applyCategory',
            dataType: 'json',
            data: {categoryId: category.id, text: text}
        });
    };

    service.createCategory = function (category, text) {
        return $http({
            method: 'POST',
            url: '/textClassification/learn',
            dataType: 'json',
            data: {categoryName: category.name, text: text}
        });
    };

    return service;
}
