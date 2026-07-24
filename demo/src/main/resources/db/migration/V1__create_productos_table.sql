CREATE TABLE IF NOT EXISTS productos (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(100)   NOT NULL,
    categoria   VARCHAR(50)    NOT NULL,
    stock       INTEGER        NOT NULL DEFAULT 0,
    precio      DECIMAL(10,2)  NOT NULL,
    activo      BOOLEAN        NOT NULL DEFAULT TRUE,
    creado_en   TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_productos_activo ON productos (activo);
CREATE INDEX IF NOT EXISTS idx_productos_categoria ON productos (categoria);

-- Datos de ejemplo para el Mercado Municipal de Quevedo
INSERT INTO productos (nombre, categoria, stock, precio, activo) VALUES
    ('Arroz Supremo 5kg',      'Granos',        120, 6.50,  TRUE),
    ('Platano Verde (unidad)', 'Frutas',        500, 0.15,  TRUE),
    ('Queso Fresco 500g',      'Lacteos',       80,  3.20,  TRUE),
    ('Cacao en Grano 1kg',     'Granos',        60,  4.75,  TRUE),
    ('Tilapia Fresca (kg)',    'Pescados',      45,  5.90,  TRUE);
