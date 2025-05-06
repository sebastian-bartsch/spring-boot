package com.tld.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tld.dto.info.LocationInfoDTO;
import com.tld.entity.Location;


public interface LocationRepository extends JpaRepository<Location, Long>{

	

	 @Query(value = """
	 		    SELECT * FROM get_active_locations (:field, :value);					
		    """, nativeQuery = true)
	 List<LocationInfoDTO> findLocations(@Param("field") String field ,@Param("value") String value);	
	 
	 /*@Query(value = """

		select location_id, company_name, location_address, 
			  location_meta,  city_name, region_name, 
			  country_name, c.user_name  as location_created_by, TO_CHAR(location_created_at, 'DD-MM-YYYY HH24:MI') AS location_crated_at,
			  m.user_name  as location_modified_by, TO_CHAR(location_modified_at, 'DD-MM-YYYY HH24:MI') AS location_modified_at,
			  location_is_active
		from location
		join company on
		location.company_id=company.company_id
		join city on
		location.city_id=city.city_id
		join region on
		city.city_id =region.region_id
		join country on
		region.country_id =country.country_id
		join users c on
		location.location_created_by =c.user_id
		join users m on
		location.location_modified_by =m.user_id
		where 
		location_id=19
""", nativeQuery = true)
	 */
	 
	 
	 
	 @Query(value = """
	 		    SELECT count(*) FROM location 
	 		    where
	 		    location_is_active= true and
	 		    company_id= :companyId and
	 		    location_id= :locationId				
		    """, nativeQuery = true)
	 Short findIfLocationAndCompanyAreOk(@Param("companyId") Long companyId, @Param("locationId") Long locationId);	
}
