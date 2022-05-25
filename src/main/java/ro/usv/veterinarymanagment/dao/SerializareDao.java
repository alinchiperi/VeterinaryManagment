package ro.usv.veterinarymanagment.dao;

import java.util.List;

public class SerializareDao <T extends Entitate, K> implements Dao<T,K>{
    @Override
    public T get(K id) {
        return null;
    }

    @Override
    public void delete(K id) {

    }

    @Override
    public void save(T t) {

    }

    @Override
    public void update(T t) {

    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public void saveAll(List<T> lst) {

    }

    @Override
    public void deleteAll() {

    }
}
