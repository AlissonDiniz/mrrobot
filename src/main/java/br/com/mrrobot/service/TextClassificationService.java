/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.service;

import br.com.mrrobot.to.CategoryTO;
import br.com.mrrobot.Config;
import br.com.mrrobot.domain.Category;
import br.com.mrrobot.domain.Text;
import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.LMClassifier;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;
import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.mrrobot.repository.CategoryRepository;
import com.aliasi.classify.ConditionalClassification;
import com.aliasi.util.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alisson Diniz
 */
@Service
public class TextClassificationService {

    @Autowired
    private CategoryRepository categoryRepository;

    private LMClassifier classifier;

    public CategoryTO classify(String text) {
        try {
            File knowledge = new File(Config.KNOWLEDGE_PATH);
            this.classifier = (LMClassifier) AbstractExternalizable.readObject(knowledge);
            ConditionalClassification conditionalClassification = this.classifier.classify(text);
            return new CategoryTO(1l, conditionalClassification.bestCategory());
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(TextClassificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void init() throws IOException, ClassNotFoundException {
        List<Category> categoryList = StreamSupport.stream(this.categoryRepository.findAll().spliterator(), false).collect(Collectors.toList());
        String[] categoryArray = categoryList.stream().map(it -> it.getName()).collect(Collectors.toList()).toArray(new String[]{});
        int nGram = 7;
        this.classifier = DynamicLMClassifier.createNGramProcess(categoryArray, nGram);
        AbstractExternalizable.compileTo((Compilable) this.classifier, new File(Config.KNOWLEDGE_PATH));
    }
    
    @SuppressWarnings("unchecked")
    private void train() throws IOException, ClassNotFoundException {
        List<Category> categoryList = StreamSupport.stream(this.categoryRepository.findAll().spliterator(), false).collect(Collectors.toList());
        if (categoryList.size() > 1) {
            String[] categoryArray = categoryList.stream().map(it -> it.getName()).collect(Collectors.toList()).toArray(new String[]{});
            int nGram = 7;
            this.classifier = DynamicLMClassifier.createNGramProcess(categoryArray, nGram);
            categoryList.forEach((Category it) -> {
                Classification classification = new Classification(it.getName());
                it.getTextList().forEach((Text tx) -> {
                    Classified classified = new Classified(tx.getReview(), classification);
                    ((ObjectHandler) this.classifier).handle(classified);
                });
            });
            AbstractExternalizable.compileTo((Compilable) this.classifier, new File(Config.KNOWLEDGE_PATH));
        }
    }

    public void learn(String categoryName, String content) {
        Category category = this.categoryRepository.findByName(categoryName);
        if(category == null){
            category = new Category(categoryName);
        }
        String review;
        try {
            File trainFile = new File(Config.TRAIN_PATH);
            Files.writeBytesToFile(content.getBytes(), trainFile);
            review = Files.readFromFile(trainFile, Config.CHARSET);
            Text text = new Text(review);
            category.getTextList().add(text);
            this.categoryRepository.save(category);
            this.train();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(TextClassificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void applyCategory(long categoryId, String content) {
        Category category = this.categoryRepository.findOne(categoryId);
        String review;
        try {
            File trainFile = new File(Config.TRAIN_PATH);
            Files.writeBytesToFile(content.getBytes(), trainFile);
            review = Files.readFromFile(trainFile, Config.CHARSET);
            Text text = new Text(review);
            category.getTextList().add(text);
            this.categoryRepository.save(category);
            this.train();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(TextClassificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
