package medictonproject.interfaces;

import medictonproject.model.FaqEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface FaqInterface {
    List getAll(int is_global);

    @Transactional
    void add(FaqEntity f);

    @Transactional
    void delete(int faqId);

    @Transactional
    FaqEntity find(int faqId);

    @Transactional
    void update(FaqEntity f);
}
