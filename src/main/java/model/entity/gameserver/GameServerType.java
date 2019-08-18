package model.entity.gameserver;

public enum GameServerType {
    RELAX(0x02),
    TEST(0x04),
    BROAD(0x08),
    CREATE_RESTRICT(0x10),
    EVENT(0x20),
    FREE(0x40),
    WORLD_RAID(0x100),
    NEW(0x200),
    CLASSIC(0x400);

    private final int mask;

    GameServerType(int _mask) {
        this.mask = _mask;
    }

    public int getMask() {
        return this.mask;
    }
}
