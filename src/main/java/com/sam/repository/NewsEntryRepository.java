package com.sam.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.sam.entities.NewsEntry;


public interface NewsEntryRepository extends ElasticsearchRepository<NewsEntry, String>{

}
