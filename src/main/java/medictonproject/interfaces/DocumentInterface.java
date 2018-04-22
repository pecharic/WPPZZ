package medictonproject.interfaces;

import medictonproject.model.DocumentEntity;
import org.springframework.transaction.annotation.Transactional;


public interface DocumentInterface {
    @Transactional
    boolean add(DocumentEntity d);
}
