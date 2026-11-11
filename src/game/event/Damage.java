package game.event;

import game.character.Character;

/**
 * Little record grouping damage info
 * @param source: the origin of the damage, needed to attribute kill xp
 * @param physical_damage
 * @param magic_damage
 */
public record Damage(Character source, int physical_damage, int magic_damage) {
}
