package com.oldlie.zshop.zshopvue.service.init;

/**
 * @author oldlie
 * @date 2020/11/1
 */

import com.oldlie.zshop.zshopvue.model.db.BaseEo;
import com.oldlie.zshop.zshopvue.model.db.IdSpecification;
import com.oldlie.zshop.zshopvue.model.db.repository.BaseRepository;

import java.util.Optional;

public abstract class InitService<E extends BaseEo, I extends Init<E>, ID, R extends BaseRepository<E, ID>>{

    protected R repository;

    /**
     * Init Config, permission or others
     * @param init item
     * @return init link
     */
    public InitService<E, I, ID, R> init(I init) {
       E entity = init.getEntity();
       Optional<E> optional = this.repository.findOne(IdSpecification.getInstance(entity.getId()));
       if (!optional.isPresent()) {
           this.repository.save(entity);
       }
       return this;
    }

    /**
     * 子类需要实现这个
     */
    public abstract void init();

    /**
     * 子类需要指定 Repository
     */
    public abstract void setRepository();
}
