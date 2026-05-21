package com.paulmathew.pulsesync.model.workspace

data class Collaborator(
    val id: String,
    val displayName: String,
    val initials: String,
    val avatarTone: CollaboratorTone
)

enum class CollaboratorTone {
    Violet,
    Green,
    Amber,
    Rose,
    Blue
}