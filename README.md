Akka supervision
=========================

A simple demonstration of how supervision works in Akka. A supervisor plays pingpong with a child actor. The child actor will randomly fail (probability .1) to send back a pong message. In that case it will be restarted by its supervisor and the ping pong continues.
