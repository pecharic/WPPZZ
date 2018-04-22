package medictonproject.interfaces;

import medictonproject.model.EquipmentEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface EquipmentInterface {
    @Transactional
    List<EquipmentEntity> getAll(Integer userId);

    EquipmentEntity getEq(Integer eqId);

    @Transactional
    boolean add(EquipmentEntity e);

    @Transactional
    void delete(EquipmentEntity e);

    @Transactional
    boolean update(EquipmentEntity e);

    @Transactional
    EquipmentEntity find(Integer eqId);
}
