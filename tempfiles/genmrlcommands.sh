#!/usr/bin/env bash

for mine in Icario Eliador navach KhazadDul
do
    # unset existing junk
    echo mrl unset $mine GOLD_ORE
    echo mrl unset $mine DIAMOND_ORE
    echo mrl unset $mine IRON_ORE
    echo mrl unset $mine LAPIS_ORE
    echo mrl unset $mine COAL_ORE
    echo mrl unset $mine STONE
    echo mrl unset $mine REDSTONE_ORE
    # set new junk
    echo mrl set $mine GOLD_ORE 20%
    echo mrl set $mine DIAMOND_ORE 1.5%
    echo mrl set $mine IRON_ORE 30%
    echo mrl set $mine LAPIS_ORE 2.5%
    echo mrl set $mine COAL_ORE 10%
    echo mrl set $mine REDSTONE_ORE 2.5%
    echo mrl set $mine STONE 33.5%
done
