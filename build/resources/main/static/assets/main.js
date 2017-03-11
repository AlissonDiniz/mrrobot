/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    
    loadCategories();
    
    var modal = {
        container: $("#category-modal"),
        categoriesContainer: $("#category-modal #categories-container"),
        input: $("#category-modal form input"),
        button: $("#category-modal form #create-button")
    };
    var classification = {
        form: $('#classification-form'),
        alertContainer: $('#classification-form #alert-container'),
        classify: $('#classification-form button#classify'),
        categoryCorrect: $('#classification-form button#category-correct'),
        categoryWrong: $('#classification-form button#category-wrong'),
        textarea: $('#classification-form textarea')
    };
    classification.textarea.focus();

    classification.classify.click(function () {
        classification.classify.prop('disabled', true);
        classification.classify.find('span.legend').text('Classifying...');
        classification.classify.find('span.glyphicon').removeClass('hide');
        classification.textarea.prop('disabled', true);
        
        $.ajax({
            type: "POST",
            url: "/text/classify",
            dataType: 'json',
            data: {text: classification.textarea.val()},
            success: function (data) {
                classification.categoryCorrect.removeClass('hide');
                classification.categoryWrong.removeClass('hide');
                classification.classify.addClass('hide');
                classification.classify.find('span.legend').text('Classify');
                classification.classify.find('span.glyphicon').addClass('hide');
                classification.alertContainer.attr('class', 'alert alert-success').find('span').text(data.name);
            }
        });
    });
    classification.categoryCorrect.click(function () {
        hideResult();
        enableClassify();
    });
    classification.categoryWrong.click(function () {
        hideResult();
        modal.input.val('');
        modal.container.modal();
    });
    modal.button.click(function () {
        createCategory();
    });

    function enableClassify() {
        classification.classify.prop('disabled', false);
        classification.classify.find('span.legend').text('Classify');
        classification.classify.find('span.glyphicon').addClass('hide');
        classification.textarea.prop('disabled', false);
        classification.textarea.val('');
        classification.textarea.focus();
    }

    function hideResult() {
        classification.categoryCorrect.addClass('hide');
        classification.categoryWrong.addClass('hide');
        classification.alertContainer.addClass('hide');
        classification.classify.removeClass('hide');
    }

    function loadCategories() {
        $.ajax({
            type: "GET",
            url: "/category/list",
            dataType: 'json',
            success: function (data) {
                modal.categoriesContainer.empty();
                $.each(data, function(index, it){
                    var button = $('<button type="button" class="btn btn-primary" data-category="' + it.id + '">' + it.name + '</button>');
                    button.click(function () {
                        applyCategory($(this).data('category'));
                    });
                    modal.categoriesContainer.append(button);
                });
            }
        });
    }

    function applyCategory(categoryId) {
        modal.container.modal("hide");
        $.ajax({
            type: "POST",
            url: "/text/applyCategory",
            dataType: 'json',
            data: {categoryId: categoryId, text: classification.textarea.val()},
            success: function () {
                enableClassify();
            }
        });
    }
    
    function createCategory(){
        modal.container.modal("hide");
        $.ajax({
            type: "POST",
            url: "/text/learn",
            dataType: 'json',
            data: {categoryName: modal.input.val(), text: classification.textarea.val()},
            success: function () {
                loadCategories();
                enableClassify();
            }
        });
    }
});

