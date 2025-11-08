(define (domain laberint)
  (:requirements :strips :typing)
  
  (:types agent lloc clau porta)
  
  (:predicates
    (a-agent ?a - agent)
    (a-loc ?a - agent ?l - lloc)
    (adjacent ?l1 - lloc ?l2 - lloc)
    (porta-tancada ?p - porta)
    (porta-oberta ?p - porta)
    (porta-a ?p - porta ?l1 - lloc ?l2 - lloc)
    (te-clau ?a - agent ?c - clau)
    (obre ?c - clau ?p - porta)
    (hi-ha-clau ?c - clau ?l - lloc)
    (sortida ?l - lloc)
  )
  
  (:action moure
    :parameters (?a - agent ?from - lloc ?to - lloc)
    :precondition (and
      (a-loc ?a ?from)
      (adjacent ?from ?to)
      (not (porta-tancada ?p))
    )
    :effect (and
      (not (a-loc ?a ?from))
      (a-loc ?a ?to)
    )
  )

  (:action agafar-clau
    :parameters (?a - agent ?c - clau ?l - lloc)
    :precondition (and
      (a-loc ?a ?l)
      (hi-ha-clau ?c ?l)
    )
    :effect (and
      (te-clau ?a ?c)
      (not (hi-ha-clau ?c ?l))
    )
  )

  (:action obrir-porta
    :parameters (?a - agent ?c - clau ?p - porta ?l1 - lloc ?l2 - lloc)
    :precondition (and
      (a-loc ?a ?l1)
      (porta-a ?p ?l1 ?l2)
      (porta-tancada ?p)
      (te-clau ?a ?c)
      (obre ?c ?p)
    )
    :effect (and
      (porta-oberta ?p)
      (not (porta-tancada ?p))
    )
  )
)
