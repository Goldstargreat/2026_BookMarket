package kr.ac.kopo.gnuyog.bookmarket.validator;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class UnitsInstockValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return Book.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        Book book = (Book) target;
        BigDecimal unitPrice = book.getUnitPrice();
        long unitsInStock = book.getUnitsInStock();
        if(unitPrice != null && unitPrice.intValue() >= 10000 && unitsInStock >= 99)
        {
            errors.rejectValue("unitsInStock", "UnitsInstockValidator.message", "가격이 10000원 이상인 경우에는 100개 이상을 재고량으로 등록할 수 없습니다. ");
        }
    }
}
