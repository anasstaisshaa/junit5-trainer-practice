package edu.AnastasiiaTkachuk.mapper;

public interface Mapper<F, T> {

    T map(F object);
}
