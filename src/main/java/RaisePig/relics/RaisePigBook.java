package RaisePig.relics;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.RaisePigPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

// 继承CustomRelic
public class RaisePigBook extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath("RaisePigBook");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "RaisePigResources/img/relics/RaisePigBook.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private int maxEnergy; // 战斗开始时的能量上限
    private int permanentEnergyIncrease = 0; // 战斗过程中永久增加的能量上限

    public RaisePigBook() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new RaisePigBook();
    }

    //编写遗物效果在触发时机编写即可
    @Override
    public void atBattleStart() {
        // 战斗开始记录当前能量上限
        super.atBattleStart();
        this.maxEnergy = AbstractDungeon.player.energy.energyMaster;
        this.permanentEnergyIncrease = 0; // 重置永久增加计数
        // 添加3层养猪状态
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new RaisePigPower(AbstractDungeon.player, 3)
                )
        );
    }

    // 当战斗过程中永久增加能量上限时调用
    public void onEnergyMasterIncrease() {
        this.permanentEnergyIncrease++;
    }

    @Override
    public void onVictory(){
        super.onVictory();
        // 计算最终的永久能量上限：初始值 + 战斗过程中永久增加的值
        int finalPermanentEnergy = this.maxEnergy + this.permanentEnergyIncrease;
        // 设置最终的永久能量上限
        AbstractDungeon.player.energy.energyMaster = finalPermanentEnergy;
        // 确保当前能量不超过上限
        if (AbstractDungeon.player.energy.energy > finalPermanentEnergy) {
            AbstractDungeon.player.energy.energy = finalPermanentEnergy;
        }
    }
}