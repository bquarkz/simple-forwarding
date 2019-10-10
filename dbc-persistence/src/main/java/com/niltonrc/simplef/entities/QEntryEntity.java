package com.niltonrc.simplef.entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QEntryEntity is a Querydsl query type for EntryEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEntryEntity extends EntityPathBase<EntryEntity> {

    private static final long serialVersionUID = -344812816L;

    public static final QEntryEntity entryEntity = new QEntryEntity("entryEntity");

    public final NumberPath<Integer> entryId = createNumber("entryId", Integer.class);

    public final StringPath fakeAddress = createString("fakeAddress");

    public final StringPath realAddress = createString("realAddress");

    public final MapPath<String, Integer, NumberPath<Integer>> statistics = this.<String, Integer, NumberPath<Integer>>createMap("statistics", String.class, Integer.class, NumberPath.class);

    public QEntryEntity(String variable) {
        super(EntryEntity.class, forVariable(variable));
    }

    public QEntryEntity(Path<? extends EntryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEntryEntity(PathMetadata metadata) {
        super(EntryEntity.class, metadata);
    }

}

