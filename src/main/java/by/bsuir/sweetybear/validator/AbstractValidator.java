package by.bsuir.sweetybear.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public abstract class AbstractValidator implements Validator {
    protected abstract String getRegex();

    @Override
    public boolean isValid(String expression) {
        Pattern pattern = Pattern.compile(getRegex());
        Matcher matcher = pattern.matcher(expression);
        return matcher.find();
    }
}
