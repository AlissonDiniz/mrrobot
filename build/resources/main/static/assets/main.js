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




//$(document).ready(function () {
//    
//    loadCategories();
//    
//    var modal = {
//        container: $("#category-modal"),
//        categoriesContainer: $("#category-modal #categories-container"),
//        input: $("#category-modal form input"),
//        button: $("#category-modal form #create-button")
//    };
//    var classification = {
//        form: $('#classification-form'),
//        alertContainer: $('#classification-form #alert-container'),
//        classify: $('#classification-form button#classify'),
//        categoryCorrect: $('#classification-form button#category-correct'),
//        categoryWrong: $('#classification-form button#category-wrong'),
//        textarea: $('#classification-form textarea')
//    };
//    classification.textarea.focus();
//
//    classification.classify.click(function () {
//        classification.classify.prop('disabled', true);
//        classification.classify.find('span.legend').text('Classifying...');
//        classification.classify.find('span.glyphicon').removeClass('hide');
//        classification.textarea.prop('disabled', true);
//        
//        $.ajax({
//            type: "POST",
//            url: "/text/classify",
//            dataType: 'json',
//            data: {text: classification.textarea.val()},
//            success: function (data) {
//                classification.categoryCorrect.removeClass('hide');
//                classification.categoryWrong.removeClass('hide');
//                classification.classify.addClass('hide');
//                classification.classify.find('span.legend').text('Classify');
//                classification.classify.find('span.glyphicon').addClass('hide');
//                classification.alertContainer.attr('class', 'alert alert-success').find('span').text(data.name);
//            }
//        });
//    });
//    classification.categoryCorrect.click(function () {
//        hideResult();
//        enableClassify();
//    });
//    classification.categoryWrong.click(function () {
//        hideResult();
//        modal.input.val('');
//        modal.container.modal();
//    });
//    modal.button.click(function () {
//        createCategory();
//    });
//
//    function enableClassify() {
//        classification.classify.prop('disabled', false);
//        classification.classify.find('span.legend').text('Classify');
//        classification.classify.find('span.glyphicon').addClass('hide');
//        classification.textarea.prop('disabled', false);
//        classification.textarea.val('');
//        classification.textarea.focus();
//    }
//
//    function hideResult() {
//        classification.categoryCorrect.addClass('hide');
//        classification.categoryWrong.addClass('hide');
//        classification.alertContainer.addClass('hide');
//        classification.classify.removeClass('hide');
//    }
//
//    function loadCategories() {
//        $.ajax({
//            type: "GET",
//            url: "/category/list",
//            dataType: 'json',
//            success: function (data) {
//                modal.categoriesContainer.empty();
//                $.each(data, function(index, it){
//                    var button = $('<button type="button" class="btn btn-primary" data-category="' + it.id + '">' + it.name + '</button>');
//                    button.click(function () {
//                        applyCategory($(this).data('category'));
//                    });
//                    modal.categoriesContainer.append(button);
//                });
//            }
//        });
//    }
//
//    function applyCategory(categoryId) {
//        modal.container.modal("hide");
//        $.ajax({
//            type: "POST",
//            url: "/text/applyCategory",
//            dataType: 'json',
//            data: {categoryId: categoryId, text: classification.textarea.val()},
//            success: function () {
//                enableClassify();
//            }
//        });
//    }
//    
//    function createCategory(){
//        modal.container.modal("hide");
//        $.ajax({
//            type: "POST",
//            url: "/text/learn",
//            dataType: 'json',
//            data: {categoryName: modal.input.val(), text: classification.textarea.val()},
//            success: function () {
//                loadCategories();
//                enableClassify();
//            }
//        });
//    }
//});
//
