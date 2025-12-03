# 🧾 Invoice Service & Carrito

**Puerto:** `8084`
**Base de Datos:** `db_invoice`

Gestiona el **Carrito de Compras** temporal y la generación definitiva de **Facturas**.

## 🛠️ Base de Datos
```sql
CREATE DATABASE db_invoice; -- Para artículos del carrito y facturas
```

## ✨ Funcionalidades Clave

### 🛒 Carrito de Compras
*   **Agregar:** Valida existencia y stock en tiempo real con `product-Service`.
*   **Consulta:** Muestra descripción y precios actualizados desde el catálogo.
*   **Gestión:** Actualiza cantidades o elimina ítems.

### 💳 Finalizar Compra (Facturación)
1.  **Validación:** Verifica stock final.
2.  **Cálculo:** Subtotal + Impuestos (16%) = Total.
3.  **Inventario:** Consume el endpoint de `product-Service` para restar el stock vendido.
4.  **Limpieza:** Vacía el carrito tras el éxito.

### 🌟 Puntos Extra Implementados
*   **Dirección Automática:** La dirección de envío se obtiene automáticamente consultando el perfil del cliente en `customer-service` para garantizar la integridad de los datos.
*   **Métodos de Pago:** Validación y persistencia.
*   **Cupones:** Lógica de descuento aplicada al total.

## 🧪 Ejemplo de Prueba (Postman)
**Endpoint:** `POST http://localhost:8084/invoice`

**Body:**
```json
{
    "payment_method": "TARJETA",
    "card_number": "1234567812345678",
    "coupon_code": "DESCUENTO20"
}
```
---
### 🔗 Mapa de Arquitectura
0. [Config data](https://github.com/AlexaRamirezV/config-data.git)
1.  [Config Server](https://github.com/AlexaRamirezV/config-service.git)
2.  [Registry Service (Eureka)](https://github.com/AlexaRamirezV/registry-service.git)
3.  [Gateway Service](https://github.com/AlexaRamirezV/gateway-service.git)
4.  [Admin Service](https://github.com/AlexaRamirezV/admin-service.git)
5.  APIs del sistema:
   * [Auth](https://github.com/AlexaRamirezV/DWB-auth.git)
   * [Customer](https://github.com/AlexaRamirezV/DWB-customer.git)
   * [Product](https://github.com/xEriis/Backend.git)
   * ➡️ **[Invoice]**
