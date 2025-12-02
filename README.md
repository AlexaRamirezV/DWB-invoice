# 🧾 Invoice Service & Carrito

**Puerto:** `8084`
**Base de Datos:** `db_invoice`

Gestiona el **Carrito de Compras** temporal y la generación definitiva de **Facturas**.

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
