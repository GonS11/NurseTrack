package com.nursetrack.repository;

import com.nursetrack.domain.model.Shift;
import com.nursetrack.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>
{
    Optional<Shift> findByIdAndDepartmentId(Long shiftId, Long departmentId);
    Optional<Shift> findByDepartmentId(Long departmentId);

    List<Shift> findAllByDepartmentId(Long id);
    List<Shift> findAllByUserId(Long nurseId);
    List<Shift> findByUserIdAndDepartmentId(Long nurseId, Long departmentId);
    List<Shift> findAllByUserIdAndDate(Long nurseId, LocalDate startDate, LocalDate endDate);

    @Query("""
            SELECT EXISTS (
                    SELECT 1 FROM Shift s
                    WHERE s.nurse.id = :nurseId
                    AND s.department.id = :departmentId
                    AND s.shiftDate = :date
                    AND (:excludeShiftId IS NULL OR s.id != :excludeShiftId)
                )
            """) //(Si hay) "Excluye de los resultados el turno con este ID, pero solo si excludeShiftId no es nulo"
    boolean hasNurseShiftConflict(@Param("nurseId") Long nurseId, @Param("departmentId") Long departmentId,
                                  @Param("date") LocalDate date, @Param("excludeShiftId") Long excludeShiftId);

    List<Shift> findByNurseAndDateRange(User requester, LocalDate startDate, LocalDate endDate);


}
