package com.tld.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
	  @Autowired
      private JdbcTemplate jdbcTemplate;
	  
	    @PostConstruct
	    public void init() {
	    	
	    	if (!doesExistAnyRecord("country")) {
	    	    jdbcTemplate.update("""
	    	        INSERT INTO country (country_id, country_name, country_is_active, country_created_at, country_modified_at) VALUES
	    	        (1, 'Chile', TRUE, now(), now()),
	    	        (2, 'Peru', TRUE, now(), now())
	    	    """);

	    	    jdbcTemplate.update("""
	    	        INSERT INTO region (region_id, region_name, country_id, region_is_active, region_created_at, region_modified_at) VALUES
	    	        (1, 'Región de Arica y Parinacota', 1, TRUE, now(), now()), 
	    	        (2, 'Región de Tarapacá', 1, TRUE, now(), now()),
	    	        (3, 'Región de Antofagasta', 1, TRUE, now(), now()),
	    	        (4, 'Región de Atacama', 1, TRUE, now(), now()), 
	    	        (5, 'Región de Coquimbo', 1, TRUE, now(), now()), 
	    	        (6, 'Región de Valparaíso', 1, TRUE, now(), now()), 
	    	        (7, 'Región Metropolitana de Santiago', 1, TRUE, now(), now()), 
	    	        (8, 'Región del Libertador General Bernardo O’Higgins', 1, TRUE, now(), now()), 
	    	        (9, 'Región de Maule', 1, TRUE, now(), now()), 
	    	        (10, 'Región de Ñuble', 1, TRUE, now(), now()), 
	    	        (11, 'Región de Biobío', 1, TRUE, now(), now()), 
	    	        (12, 'Región de La Araucanía', 1, TRUE, now(), now()), 
	    	        (13, 'Región de Los Ríos', 1, TRUE, now(), now()), 
	    	        (14, 'Región de Los Lagos', 1, TRUE, now(), now()), 
	    	        (15, 'Región de Aysén del General Carlos Ibáñez del Campo', 1, TRUE, now(), now()), 
	    	        (16, 'Región de Magallanes y de la Antártica Chilena', 1, TRUE, now(), now()), 
	    	        (17, 'Amazonas', 2, TRUE, now(), now()), 
	    	        (18, 'Áncash', 2, TRUE, now(), now()), 
	    	        (19, 'Apurímac', 2, TRUE, now(), now()), 
	    	        (20, 'Arequipa', 2, TRUE, now(), now()), 
	    	        (21, 'Ayacucho', 2, TRUE, now(), now()), 
	    	        (22, 'Cajamarca', 2, TRUE, now(), now()),
	    	        (23, 'Callao', 2, TRUE, now(), now()), 
	    	        (24, 'Cusco', 2, TRUE, now(), now()), 
	    	        (25, 'Huancavelica', 2, TRUE, now(), now()), 
	    	        (26, 'Huánuco', 2, TRUE, now(), now()), 
	    	        (27, 'Ica', 2, TRUE, now(), now()), 
	    	        (28, 'Junín', 2, TRUE, now(), now()), 
	    	        (29, 'La Libertad', 2, TRUE, now(), now()), 
	    	        (30, 'Lambayeque', 2, TRUE, now(), now()), 
	    	        (31, 'Lima', 2, TRUE, now(), now()), 
	    	        (32, 'Loreto', 2, TRUE, now(), now()), 
	    	        (33, 'Madre de Dios', 2, TRUE, now(), now()), 
	    	        (34, 'Moquegua', 2, TRUE, now(), now()), 
	    	        (35, 'Pasco', 2, TRUE, now(), now()), 
	    	        (36, 'Piura', 2, TRUE, now(), now()), 
	    	        (37, 'Puno', 2, TRUE, now(), now()), 
	    	        (38, 'San Martín', 2, TRUE, now(), now()), 
	    	        (39, 'Tacna', 2, TRUE, now(), now()), 
	    	        (40, 'Tumbes', 2, TRUE, now(), now()), 
	    	        (41, 'Ucayali', 2, TRUE, now(), now());
	    	    """);

	    	    jdbcTemplate.update("""
	    	        INSERT INTO city (city_name, region_id, city_is_active, city_created_at, city_modified_at) VALUES
	    	        ('Lima', 31, true, now(), now()),
	    	        ('Arequipa', 20, true, now(), now()),
	    	        ('Trujillo', 29, true, now(), now()),
	    	        ('Chiclayo', 28, true, now(), now()),
	    	        ('Piura', 36, true, now(), now()),
	    	        ('Cusco', 24, true, now(), now()),
	    	        ('Iquitos', 32, true, now(), now()),
	    	        ('Huancayo', 13, true, now(), now()),
	    	        ('Tacna', 39, true, now(), now()),
	    	        ('Puno', 37, true, now(), now()),
	    	        ('Calama', 3, true, now(), now()),
	    	        ('Chuquicamata', 3, true, now(), now()),
	    	        ('Copiapó', 4, true, now(), now()),
	    	        ('Tierra Amarilla', 4, true, now(), now()),
	    	        ('El Salvador', 4, true, now(), now()),
	    	        ('Vallenar', 4, true, now(), now()),
	    	        ('Diego de Almagro', 4, true, now(), now()),
	    	        ('Concepción', 11, true, now(), now()),
	    	        ('Temuco', 12, true, now(), now()),
	    	        ('Valdivia', 13, true, now(), now()),
	    	        ('Antofagasta', 3, true, now(), now()),
	    	        ('Puerto Montt', 14, true, now(), now()),
	    	        ('Castro', 14, true, now(), now()),
	    	        ('Coyhaique', 15, true, now(), now()),
	    	        ('Punta Arenas', 16, true, now(), now()),
	    	        ('Iquique', 2, true, now(), now()),
	    	        ('Arica', 1, true, now(), now());
	    	    """);
	    	}     

	    	if (!doesExistAnyRecord("category")) {
	    	    jdbcTemplate.update("""
	    	        INSERT INTO category (category_id, category_name, category_is_active, category_created_at, category_modified_at) VALUES
	    	        (1, 'ESP32', true, now(), now()), 
	    	        (2, 'Zigbee', true, now(), now());
	    	    """);
	    	}

	    	if (!doesExistAnyRecord("role")) {
	    	    jdbcTemplate.update("""
	    	        INSERT INTO role (role_id, role_name, role_is_active, role_created_at, role_modified_at) VALUES
	    	        (1, 'ROLE_administrador', true, now(), now()), 
	    	        (2, 'ROLE_operario', true, now(), now());
	    	    """);
	    	}

	    	if (!doesExistAnyRecord("permission")) {
	    	    jdbcTemplate.update("""
	    	        INSERT INTO permission (permission_id, permission_name, permission_created_at, permission_modified_at) VALUES
	    	        (1, 'Insertar', now(), now()), 
	    	        (2, 'Actualizar', now(), now()), 
	    	        (3, 'Borrar', now(), now()), 
	    	        (4, 'Leer', now(), now());
	    	    """);
	    	}

	        /*Admin tiene acceso a todo, operario solo a leer*/
	        if (!doesExistAnyRecord("role_permission")) {
	        	jdbcTemplate.update("INSERT INTO role_permission (role_id, permission_id) VALUES (1,1),(1,2),(1,3),(1,4), (2,4) ;");
	        }      
	        
	        if (!doesExistAnyRecord("users")) {
	            jdbcTemplate.update("""
	                INSERT INTO users (
	                    account_non_expired, 
	                    account_non_locked, 
	                    credentials_non_expired, 
	                    user_enabled, 
	                    user_name, 
	                    user_password, 
	                    user_created_at, 
	                    user_modified_at
	                ) VALUES 
	                (TRUE, TRUE, TRUE, TRUE, 'sebastian', '$2a$10$oqdyssusFOvctG0qbNrFQOsTQHyd.cPBVX.lBrGxzqQn.RS3c4GmK', NOW(), NOW()),
	                (TRUE, TRUE, TRUE, TRUE, 'luis', '$2a$10$oqdyssusFOvctG0qbNrFQOsTQHyd.cPBVX.lBrGxzqQn.RS3c4GmK', NOW(), NOW()),
	                (TRUE, TRUE, TRUE, TRUE, 'cristian', '$2a$10$oqdyssusFOvctG0qbNrFQOsTQHyd.cPBVX.lBrGxzqQn.RS3c4GmK', NOW(), NOW()),
	                (TRUE, TRUE, TRUE, TRUE, 'manuel', '$2a$10$oqdyssusFOvctG0qbNrFQOsTQHyd.cPBVX.lBrGxzqQn.RS3c4GmK', NOW(), NOW()),
	                (TRUE, TRUE, TRUE, TRUE, 'alexis', '$2a$10$oqdyssusFOvctG0qbNrFQOsTQHyd.cPBVX.lBrGxzqQn.RS3c4GmK', NOW(), NOW());
	            """);
	        }

	        if (!doesExistAnyRecord("users_role")) {
	        	jdbcTemplate.update("""
	        			 INSERT INTO users_role (user_id,role_id)  
	        			     		      VALUES   ((select user_id from users where user_name='sebastian'),1),
			        			     		       ((select user_id from users where user_name='luis'),1),
			        			     		       ((select user_id from users where user_name='cristian'),1),
			        			     		       ((select user_id from users where user_name='manuel'),1),
			        			     		       ((select user_id from users where user_name='alexis'),1);			
	        				""");	        			
	        } 
	        
	        if (!doesExistAnyRecord("company")) {
	            jdbcTemplate.update("""
	                INSERT INTO company (
	                    company_name, 
	                    company_api_key, 
	                    company_created_by, 
	                    company_is_active, 
	                    company_modified_by, 
	                    company_created_at, 
	                    company_modified_at
	                ) VALUES 
	                ('company AAA', 'API-12345-ABS', (SELECT user_id FROM users WHERE user_name='sebastian'), TRUE, (SELECT user_id FROM users WHERE user_name='sebastian'), NOW(), NOW()),
	                ('company BBB', 'API-12345-DEP', (SELECT user_id FROM users WHERE user_name='luis'), TRUE, (SELECT user_id FROM users WHERE user_name='luis'), NOW(), NOW()),
	                ('company CCC', 'API-12345-ASR', (SELECT user_id FROM users WHERE user_name='cristian'), TRUE, (SELECT user_id FROM users WHERE user_name='cristian'), NOW(), NOW());
	            """);
	        }
	   
	        //Procedimiento para el metodo GET de location dinamico
	        if (!doesFunctionExist("get_active_locations")) {
	            jdbcTemplate.execute("""
	                    CREATE OR REPLACE FUNCTION get_active_locations(
	                    identificador VARCHAR,
	                    valor VARCHAR
	                )
	                RETURNS TABLE (
	            		location_id BIGINT,    
	                    company_name VARCHAR(255),
	                    location_address VARCHAR(255),
	                    city_name VARCHAR(255),
	                    region_name VARCHAR(255),
	                    country_name VARCHAR(255),
	                    location_meta VARCHAR(255),
	                    location_created_by VARCHAR(255),
	                    location_created_at VARCHAR(255),
	                    location_modified_by VARCHAR(255),
	                    location_modified_at VARCHAR(255),
	                    is_location_active boolean
	                ) AS $$
	                DECLARE
	                    query TEXT;
	                    column_name TEXT;
	                BEGIN
	                    IF LOWER(identificador) = 'ciudad' THEN
	                        column_name := 'city_name';
	                    ELSIF LOWER(identificador) = 'pais' THEN
	                        column_name := 'country_name';
	                    ELSIF LOWER(identificador) = 'direccion' THEN
	                        column_name := 'location_address';
	                    ELSIF LOWER(identificador) = 'id' THEN
	                        column_name := 'location_id';
	                    ELSIF LOWER(identificador) = 'usuario' THEN
	                        column_name := 'c.user_name';
	                    ELSE
	                        column_name := NULL;
	                    END IF;

	                    query := 'SELECT location.location_id,
	            				         company.company_name, 
	                                     location.location_address, 
	                                     city.city_name,
	                                     region.region_name, 
	                                     country.country_name, 
	                                     location.location_meta,
	                                     c.user_name AS location_created_by, 
	                                     TO_CHAR(location_created_at AT TIME ZONE ''America/Santiago'', ''DD-MM-YYYY HH24:MI'')::VARCHAR(255) AS location_created_at,
	                                     m.user_name AS location_modified_by,
	                                     TO_CHAR(location_modified_at AT TIME ZONE ''America/Santiago'', ''DD-MM-YYYY HH24:MI'')::VARCHAR(255) AS location_modified_at,
	                                     location_is_active
	                              FROM location
	                              JOIN company ON company.company_id = location.company_id
	                              JOIN city ON city.city_id = location.city_id 
	                              JOIN region ON region.region_id = city.region_id  
	                              JOIN country ON country.country_id = region.country_id
	                              JOIN users c ON c.user_id = location.location_created_by
	                              JOIN users m ON m.user_id = location.location_modified_by ';

	                    IF column_name IS NOT NULL AND COALESCE(valor, '') <> '' THEN
	                        IF column_name = 'location_id' THEN
	                            query := query || ' WHERE ' || column_name || ' = ' || valor::BIGINT;
	                        ELSIF column_name = 'c.user_name' THEN
	                            query := query || ' WHERE c.user_name ILIKE ' || quote_literal('%'|| valor || '%');
	                        ELSE
	                            query := query || ' WHERE ' || quote_ident(column_name) || ' ILIKE ' || quote_literal('%'||valor || '%');
	                        END IF;
	                    END IF;

	                    RETURN QUERY EXECUTE query;
	                END;
	                $$ LANGUAGE plpgsql;

	            """);
	        }
    
	        if (!doesFunctionExist("get_active_companies")) {
	            jdbcTemplate.execute(
				"""
	            		CREATE OR REPLACE FUNCTION get_active_companies(
			        	    identificador VARCHAR,
			        	    valor VARCHAR
			        	)
			        	RETURNS TABLE (
			        	    company_id BIGINT,
			        	    company_name VARCHAR(255),
			        	    company_api_key VARCHAR(255),
			        	    userNameC VARCHAR(255),
			        	    company_created_at VARCHAR(255),  
			        	    userNameM VARCHAR(255),
			        	    company_modified_at VARCHAR(255),
			        	    is_company_active  boolean
			        	) AS $$
			        	DECLARE
			        	    query TEXT;
			        	    column_name TEXT;
			        	BEGIN
			        	    -- Determinar la columna a filtrar
			        	    IF LOWER(identificador) = 'id' THEN
			        	        column_name := 'company_id';
			        	    ELSIF LOWER(identificador) = 'nombre' THEN
			        	        column_name := 'company_name';
			        	    ELSIF LOWER(identificador) = 'apikey' THEN
			        	        column_name := 'company_api_key';
			        	    ELSIF LOWER(identificador) = 'usuario' THEN
			        	        column_name := 'c.user_name';
			        	    ELSE
			        	        column_name := NULL;
			        	    END IF;
		
			        	    -- Construcción de la consulta base
			        	    query := 'SELECT company.company_id, company.company_name, company.company_api_key,
			        	                     c.user_name AS userNameC,
			        	                     TO_CHAR(company.company_created_at AT TIME ZONE ''America/Santiago'', ''DD-MM-YYYY HH24:MI'')::VARCHAR(255) AS company_created_at,			        	                      
			        	                     m.user_name AS userNameM,			        	                     
			        	                     TO_CHAR(company.company_modified_at AT TIME ZONE ''America/Santiago'', ''DD-MM-YYYY HH24:MI'')::VARCHAR(255) AS company_modified_at,			        	                     
			        	                     company.company_is_active
			        	              FROM company 
			        	              JOIN users c ON company.company_created_by = c.user_id
			        	              JOIN users m ON company.company_modified_by = m.user_id ';
		
			        	    -- Aplicar el filtro solo si valor no está vacío
			        	IF column_name IS NOT NULL AND COALESCE(valor, '') <> '' THEN
	            		 	IF column_name = 'company_id' THEN
			        	       query := query || ' WHERE ' ||  column_name || ' = ' || valor::BIGINT;
			        	    ELSIF column_name = 'c.user_name' THEN
			        	       query := query || ' WHERE ' || ' c.user_name ILIKE ' || quote_literal('%' || valor || '%');
			        	    ELSE
			        	       query := query || ' WHERE ' ||  quote_ident(column_name) || ' ILIKE ' || quote_literal('%' || valor || '%');
			        	    END IF;
				        	END IF;
			
				        	-- Ejecutar la consulta y retornar los resultados
				        	RETURN QUERY EXECUTE query;
				        	END;
				        	$$ LANGUAGE plpgsql;
	         """   );
	        }
	        
	        
	        
	        if (!doesFunctionExist("get_active_sensors")) {
	        	jdbcTemplate.execute("""
	        		  CREATE OR REPLACE FUNCTION get_active_sensors(
	                    identificador VARCHAR,
	                    valor VARCHAR,
						company_api_key VARCHAR
	                )
	                RETURNS TABLE (
	            		sensor_id BIGINT,    
	                    sensor_name VARCHAR(255),
						sensor_api_key VARCHAR(255),
	                    location_id BIGINT,    
	                    location_address VARCHAR(500),
	                    company_id BIGINT,    
	                    company_name VARCHAR(255),
	                    sensor_total_records BIGINT,
	                    sensor_created_at VARCHAR(255),	                    
	                    sensor_modified_at VARCHAR(255),
	                    sensor_is_active BOOLEAN
	                ) AS $$
	                DECLARE
	                    query TEXT;
	                    column_name TEXT;
	                BEGIN
						IF LOWER(identificador) = 'id' THEN
	                        column_name := 'sensor.sensor_id';	               
	                    ELSIF LOWER(identificador) = 'company' THEN
	                        column_name := 'location.company_id';
	                    ELSIF LOWER(identificador) = 'city' THEN
	                        column_name := 'city.city_id';
	                    ELSIF LOWER(identificador) = 'country' THEN
	                        column_name := 'country.country_id';
	                    ELSE
	                        column_name := NULL;
	                    END IF;

		                    query := ' select sensor.sensor_id, 
									    sensor_name, 
							            sensor.sensor_api_key ,
								  		sensor.location_id, 
										CONCAT(location_address, '', '', city_name, '', '', region_name, '', '', country_name)::VARCHAR(500) AS location_address, 
										location.company_id, 									    
										company_name,  		 
								  		COALESCE(measurement_summary.sensor_total_measurements, 0) AS sensor_total_measurements,
								  		TO_CHAR(sensor_created_at AT TIME ZONE ''America/Santiago'', ''DD-MM-YYYY HH24:MI'')::VARCHAR(255) AS sensor_created_at,							  		 
										TO_CHAR(sensor_modified_at AT TIME ZONE ''America/Santiago'', ''DD-MM-YYYY HH24:MI'')::VARCHAR(255) AS sensor_modified_at,
										sensor_is_active 		 
								  from sensor 
								  join location on
								  sensor.location_id =location.location_id
								  join city on
								  location.city_id=city.city_id
								  join region on
								  city.region_id =region.region_id
								  join country on
								  region.country_id =country.country_id
								  join company on
								  location.company_id=company.company_id
								  LEFT JOIN (
											    SELECT sensor_id, count(*) AS sensor_total_measurements
											    FROM measurement
												WHERE measurement_is_active=true
											    GROUP BY sensor_id
											) measurement_summary ON sensor.sensor_id = measurement_summary.sensor_id	
								WHERE
								company.company_api_key= '|| quote_literal(company_api_key);
	
		                    	IF column_name IS NOT NULL AND COALESCE(valor, '') <> '' THEN
			                        IF column_name in ('sensor.sensor_id','location.company_id','city.city_id','country.country_id')  THEN
			                            query := query ||' AND '||  column_name || ' = ' || valor::BIGINT;
	                    			END IF;
								END IF;
	                    RETURN QUERY EXECUTE query;
	                END;
	                $$ LANGUAGE plpgsql;
	        			
	        			""");
	        }
	        
	        
	 
	    }
	    public boolean doesExistAnyRecord(String tableName) {	
	    	try {
	    		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class) > 0;	    		
	    	} catch (Exception e) {	            
	            System.err.println("Error al verificar existencia de registros en la tabla " + tableName + ": " + e.getMessage());
	            return false;
	        }
	    }
	    
	    // Método para verificar si la función existe
	    private boolean doesFunctionExist(String functionName) {
	        String query = "SELECT COUNT(*) FROM pg_proc WHERE proname = ?";
	        Integer count = jdbcTemplate.queryForObject(query, Integer.class, functionName);
	        return count != null && count > 0;
	    }

	    // Método para verificar si el trigger existe
	    private boolean doesTriggerExist(String triggerName) {
	        String query = "SELECT COUNT(*) FROM pg_trigger WHERE tgname = ?";
	        Integer count = jdbcTemplate.queryForObject(query, Integer.class, triggerName);
	        return count != null && count > 0;
	    }

}
