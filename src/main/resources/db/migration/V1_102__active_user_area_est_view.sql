CREATE VIEW ACTIVE_USER_AREA_EST_VW
(OASYS_USER_CODE,CT_AREA_EST_CODE,AREA_EST_NAME)
AS
SELECT DISTINCT oasys_user_code, ct_area_est_code, area_est_name
FROM active_user_area_est_role_vw eur
WHERE EXISTS
    (SELECT 1
     FROM user_area uar
     WHERE uar.oasys_user_code = eur.oasys_user_code
       AND uar.ct_area_est_code = eur.ct_area_est_code
       AND SYSDATE BETWEEN TRUNC (uar.start_date)
         AND NVL (TRUNC (uar.end_date),
                  SYSDATE))
  AND EXISTS
    (SELECT 1
     FROM user_team utm
     WHERE utm.oasys_user_code = eur.oasys_user_code
       AND utm.ct_area_est_code = eur.ct_area_est_code
       AND SYSDATE BETWEEN TRUNC (utm.start_date)
         AND NVL (TRUNC (utm.end_date),
                  SYSDATE));