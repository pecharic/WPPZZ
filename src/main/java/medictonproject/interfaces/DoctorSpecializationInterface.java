package medictonproject.interfaces;

import medictonproject.model.DoctorspecializationEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoctorSpecializationInterface
{
  @Transactional
  DoctorspecializationEntity getSpecialization( Integer id );
  
  @Transactional
  List<DoctorspecializationEntity> getSpecializations();
  
  @Transactional
  Object getSpecialization( String spec );
  
  @Transactional
  void update( DoctorspecializationEntity doc );
}
