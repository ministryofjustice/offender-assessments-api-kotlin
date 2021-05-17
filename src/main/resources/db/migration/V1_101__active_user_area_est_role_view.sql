CREATE VIEW ACTIVE_USER_AREA_EST_ROLE_VW
(OASYS_USER_CODE,CT_AREA_EST_CODE,AREA_EST_NAME,AREA_EST_START_DATE,AREA_EST_END_DATE,REF_ROLE_CODE,ROLE_START_DATE,ROLE_END_DATE,REF_ROLE_DESC)
AS
SELECT ou.oasys_user_code,
       eur.ct_area_est_code,
       cae.area_est_name,
       cae.start_date area_est_start_date,
       cae.end_date area_est_end_date,
       eur.ref_role_code,
       eur.start_date role_start_date,
       eur.end_date role_end_date,
       rol.ref_role_desc
FROM area_est_user_role eur,
     ct_area_est cae,
     oasys_user ou,
     ref_role rol
WHERE     rol.ref_role_code = eur.ref_role_code
  AND cae.ct_area_est_code = eur.ct_area_est_code
  AND eur.oasys_user_code = ou.oasys_user_code
  AND cae.ct_area_est_code = eur.ct_area_est_code
  AND rol.role_type_elm = 'RBAC';