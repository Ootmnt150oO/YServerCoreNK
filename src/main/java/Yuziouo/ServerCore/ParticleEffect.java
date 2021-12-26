package Yuziouo.ServerCore;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.level.particle.EnchantmentTableParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;

import java.util.ArrayList;
import java.util.Random;

public class ParticleEffect {
    public static void toLight(int damage, Entity entity) {
        FullChunk chunk = entity.getLevel().getChunk((int) entity.getX() >> 4, (int) entity.getZ() >> 4);
        CompoundTag nbt = new CompoundTag();
        nbt.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("", entity.x))
                .add(new DoubleTag("", entity.y))
                .add(new DoubleTag("", entity.z)));
        nbt.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("", 0.0D))
                .add(new DoubleTag("", 0.0D))
                .add(new DoubleTag("", 0.0D))
        );
        nbt.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("", 0.0F))
                .add(new FloatTag("", 0.0F))
        );
        EntityLightning lightning = new EntityLightning(chunk, nbt);
        lightning.attack((float) damage);
        lightning.setEffect(false);
        lightning.spawnToAll();
    }

    public static void explode(Position pos, double size, Entity entity) {
        Explosion explosion = new Explosion(pos, size, entity);
        explosion.explodeB();
    }

    public static void around(Player player, int size, double damage) {
        for (Entity entitys : player.getLevel().getEntities()) {
            if (entitys instanceof Player) {
                continue;
            }
            if (entitys.distance(player.getPosition()) < size) {
                entitys.attack(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (int) damage));
            }
        }
    }

    public static void spawnFirework(Position position) {
        Level level = position.getLevel();
        ItemFirework item = new ItemFirework();
        CompoundTag tag = new CompoundTag();
        Random random = new Random();
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putByteArray("FireworkFade", new byte[0]);
        compoundTag.putBoolean("FireworkFlicker", random.nextBoolean());
        compoundTag.putBoolean("FireworkTrail", random.nextBoolean());
        tag.putCompound("Fireworks", (new CompoundTag("Fireworks")).putList((new ListTag("Explosions")).add((Tag) compoundTag)).putByte("Flight", 1));
        item.setNamedTag(tag);
        CompoundTag nbt = new CompoundTag();
        nbt.putList((new ListTag("Pos"))
                .add((Tag) new DoubleTag("", position.x + 1.0D))
                .add((Tag) new DoubleTag("", position.y + 1.0D))
                .add((Tag) new DoubleTag("", position.z + 1.0D)));
        nbt.putList((new ListTag("Motion"))
                .add((Tag) new DoubleTag("", 0.0D))
                .add((Tag) new DoubleTag("", 0.0D))
                .add((Tag) new DoubleTag("", 0.0D)));
        nbt.putList((new ListTag("Rotation"))
                .add((Tag) new FloatTag("", 0.0F))
                .add((Tag) new FloatTag("", 0.0F)));
        EntityFirework entity = new EntityFirework((FullChunk) level.getChunk((int) position.x >> 4, (int) position.z >> 4), nbt);
        entity.spawnToAll();
    }

    public static void addHealth(Position position) {
        ArrayList<Double[]> pos = new ArrayList<>();
        double sin1 = Math.sin(Math.toRadians(18));
        double cos1 = Math.cos(Math.toRadians(18));
        double sin2 = Math.sin(Math.toRadians(36));
        double cos2 = Math.cos(Math.toRadians(36));
        double l = 2 * 1.5 * cos1;
        for (double i = -1.5 * cos1; i <= 1.5 * cos1; i += 0.1) {
            pos.add(new Double[]{i, 0.0, 1.5 * sin1});
        }

        for (double i = 0; i <= l; i += 0.1) {
            double x = i * cos1;
            double z = i * sin1;
            pos.add(new Double[]{-z, 0.0, 1.5 - x});
            pos.add(new Double[]{z, 0.0, 1.5, -x});
            x = i * cos2;
            z = i * sin2;
            pos.add(new Double[]{1.5 * cos1 - x, 0.0, 1.5 * sin1 - z});
            pos.add(new Double[]{-1.5 * cos1 + x, 0.0, 1.5 * sin1 - z});
        }
        for (Double[] xyz : pos) {
            position.level.addParticle(new DustParticle(new Vector3(xyz[0] + position.x, xyz[1] + position.y + 1, xyz[2] + position.z), 250, 0, 0, 250));
        }
    }

    public static void addIce(Position position) {

        ArrayList<Double[]> a = new ArrayList<>();
        ArrayList<Double[]> pos = new ArrayList<>();
        double rr = 1.5 * 0.2;
        for (int i = 0; i <= 90; i += 10) {
            double x = rr * Math.cos(Math.toRadians(i));
            double y = rr * Math.sin(Math.toRadians(i));
            a.add(new Double[]{x, y});
            a.add(new Double[]{x, -y});
            a.add(new Double[]{-x, y});
            a.add(new Double[]{-x, -y});
        }
        for (Double[] b : a) {
            for (int i = 0; i <= 90; i += 10) {
                double x = (1.5 - b[0]) * Math.cos(Math.toRadians(i));
                double z = (1.5 - b[0]) * Math.sin(Math.toRadians(i));
                pos.add(new Double[]{x, b[1], z});
                pos.add(new Double[]{-z, b[1], x});
                pos.add(new Double[]{-x, b[1], -z});
                pos.add(new Double[]{z, b[1], -x});
            }
        }
        for (Double[] xyz : pos) {
            position.level.addParticle(new DustParticle(new Vector3(xyz[0] + position.x, xyz[1] + position.y + 2, xyz[2] + position.z), 0, 0, 250, 250));
        }
    }

    public static void addRelief(Position position) {
        ArrayList<Double[]> a = new ArrayList<>();
        ArrayList<Double[]> pos = new ArrayList<>();
        for (int i = 0; i <= 90; i += 9) {
            double x = 1.5 * Math.cos(Math.toRadians(i));
            double y = 1.5 * Math.sin(Math.toRadians(i));
            a.add(new Double[]{x, +y});
            a.add(new Double[]{x, -y});
        }

        for (Double[] b : a) {
            for (int i = 0; i <= 90; i += 9) {
                double x = b[0] * Math.cos(Math.toRadians(i));
                double z = b[0] * Math.sin(Math.toRadians(i));
                pos.add(new Double[]{x, b[1], z});
                pos.add(new Double[]{-z, b[1], x});
                pos.add(new Double[]{-x, b[1], -z});
                pos.add(new Double[]{z, b[1], -x});
            }
        }
        for (Double[] xyz : pos) {
            position.level.addParticle(new EnchantmentTableParticle(new Vector3(xyz[0] + position.x, xyz[1] + position.y + 1, xyz[2] + position.z)));
        }
    }

    public static void addMaxDamage(Position position) {
        Level level = position.level;
        level.addSound(position, Sound.MOB_GHAST_CHARGE);
        double x = position.x;
        double y = position.y;
        double z = position.z;
        double posA, posB, posC;
        posC = y;
        for (int i = 1; i <= 100; i++) {
            posA = x + 3 * Math.cos(i * 3.14 / 36);
            posB = z + 3 * Math.sin(i * 3.14 / 36);
            level.addParticle(new DustParticle(new Vector3(posA, posC, posB), 155, 0, 112));
            posC += 0.015;

        }
        posC = y;
        for (int i = 1; i <= 100; i++) {
            posA = x + 2 * Math.cos(i * 3.14 / 36);
            posB = z + 2 * Math.sin(i * 3.14 / 36);
            level.addParticle(new DustParticle(new Vector3(posA, posC, posB), 200, 0, 0));
            posC += 0.015;
        }
        posC = y;
        for (int i = 1; i <= 100; i++) {
            posA = x + 1 * Math.cos(i * 3.14 / 36);
            posB = x + 1 * Math.sin(i * 3.14 / 36);
            level.addParticle(new DustParticle(new Vector3(posA, posC, posB), 255, 0, 0));
            posC += 0.015;
        }


    }
}
