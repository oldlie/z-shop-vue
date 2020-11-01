package com.oldlie.zshop.zshopvue.service.init;

/**
 * @author oldlie
 * @date 2020/11/1
 */

import com.oldlie.zshop.zshopvue.model.db.KeyEntity;
import com.oldlie.zshop.zshopvue.model.db.repository.BaseRepository;
import com.oldlie.zshop.zshopvue.model.db.specification.KeyValueSpecification;

import java.util.Optional;

public abstract class InitService<E extends KeyEntity, I extends InitBase<E>, ID, R extends BaseRepository<E, ID>> {

    protected R repository;

    /**
     * Init Config, permission or others
     *
     * @param init item
     * @return init link
     */
    public InitService<E, I, ID, R> init(I init) {
        if (this.repository == null) {
            synchronized (this) {
                if (this.repository == null) {
                    this.setRepository();
                }
            }
        }
        E entity = init.getEntity();
        Optional<E> optional =
                this.repository.findOne(KeyValueSpecification.getInstance(KeyEntity.KEY, entity.getKey()));
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
