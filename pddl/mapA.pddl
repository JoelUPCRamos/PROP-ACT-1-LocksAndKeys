(define (problem mapa-a)
  (:domain locks-keys)

  (:objects
    p1 p2 - agent
    ka - key
    DA - door
    r1c1 r1c2 r1c3 r1c4 r1c5
    r2c1 r2c2 r2c3 r2c4 r2c6
    r3c1 r3c2 r3c3 r3c4 r3c5 r3c6 - cell
  )

  (:init
    ;; celdas vacías (no llave/puerta/salida)
    (empty r1c1) (empty r1c2) (empty r1c3) (empty r1c4) (empty r1c5)
    (empty r2c2) (empty r2c3) (empty r2c4)
    (empty r3c1) (empty r3c2) (empty r3c3) (empty r3c4) (empty r3c5)

    ;; llave, puerta y salida
    (key-at ka r2c1)
    (door-at DA r3c6)
    (opens ka DA)
    (exit r2c6)

    ;; posiciones iniciales de agentes y ocupación
    (at p1 r3c4) (occupied r3c4)   ; agente '1'
    (at p2 r1c2) (occupied r1c2)   ; agente '2'

    ;; adyacencias 4-dir
    ; fila 1
    (adj r1c1 r1c2) (adj r1c2 r1c1)
    (adj r1c2 r1c3) (adj r1c3 r1c2)
    (adj r1c3 r1c4) (adj r1c4 r1c3)
    (adj r1c4 r1c5) (adj r1c5 r1c4)
    (adj r1c2 r2c2) (adj r2c2 r1c2)

    ; fila 2
    (adj r2c1 r2c2) (adj r2c2 r2c1)
    (adj r2c2 r2c3) (adj r2c3 r2c2)
    (adj r2c3 r2c4) (adj r2c4 r2c3)
    (adj r2c2 r3c2) (adj r3c2 r2c2)
    (adj r2c3 r1c3) (adj r1c3 r2c3)
    (adj r2c4 r1c4) (adj r1c4 r2c4)
    (adj r2c4 r3c4) (adj r3c4 r2c4)
    (adj r2c6 r3c6) (adj r3c6 r2c6)

    ; fila 3
    (adj r3c1 r3c2) (adj r3c2 r3c1)
    (adj r3c2 r3c3) (adj r3c3 r3c2)
    (adj r3c3 r3c4) (adj r3c4 r3c3)
    (adj r3c4 r3c5) (adj r3c5 r3c4)
    (adj r3c1 r2c1) (adj r2c1 r3c1)
    (adj r3c2 r2c2) (adj r2c2 r3c2)
    (adj r3c3 r2c3) (adj r2c3 r3c3)
    (adj r3c5 r3c6) (adj r3c6 r3c5)
  )

  (:goal (exists (?a - agent ?e - cell) (and (exit ?e) (at ?a ?e))))
)
