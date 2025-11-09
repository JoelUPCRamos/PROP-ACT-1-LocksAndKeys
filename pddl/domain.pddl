(define (domain locks-keys)
  (:requirements :strips :typing :equality)
  (:types agent cell key door)

  (:predicates
    (at ?a - agent ?c - cell)
    (occupied ?c - cell)
    (adj ?from - cell ?to - cell)
    (exit ?c - cell)
    (empty ?c - cell)
    (key-at ?k - key ?c - cell)
    (has ?k - key)                ; llaves compartidas por todos
    (door-at ?d - door ?c - cell)
    (opens ?k - key ?d - door)
  )

  (:action move-empty
    :parameters (?a - agent ?from - cell ?to - cell)
    :precondition (and (at ?a ?from) (adj ?from ?to) (empty ?to) (not (occupied ?to)))
    :effect (and (not (at ?a ?from)) (at ?a ?to)
                 (not (occupied ?from)) (occupied ?to)))

  (:action move-pick
    :parameters (?a - agent ?from - cell ?to - cell ?k - key)
    :precondition (and (at ?a ?from) (adj ?from ?to) (key-at ?k ?to) (not (occupied ?to)))
    :effect (and (not (at ?a ?from)) (at ?a ?to)
                 (not (occupied ?from)) (occupied ?to)
                 (has ?k) (not (key-at ?k ?to)) (empty ?to)))

  (:action move-door
    :parameters (?a - agent ?from - cell ?to - cell ?d - door ?k - key)
    :precondition (and (at ?a ?from) (adj ?from ?to)
                       (door-at ?d ?to) (opens ?k ?d) (has ?k)
                       (not (occupied ?to)))
    :effect (and (not (at ?a ?from)) (at ?a ?to)
                 (not (occupied ?from)) (occupied ?to)))

  (:action move-exit
    :parameters (?a - agent ?from - cell ?to - cell)
    :precondition (and (at ?a ?from) (adj ?from ?to) (exit ?to) (not (occupied ?to)))
    :effect (and (not (at ?a ?from)) (at ?a ?to)
                 (not (occupied ?from)) (occupied ?to)))
)
