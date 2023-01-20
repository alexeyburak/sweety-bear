package by.bsuir.sweetybear.validator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@FunctionalInterface
public interface Validator<T> {
    boolean isValid(T expression);
}
