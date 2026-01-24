package RaisePig.powers;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.IncreaseMaxEnergyAction;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RaisePigPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("RaisePigPower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    // 层数上限
    private static final int MAX_STACKS = 10;

    public RaisePigPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 限制层数不超过上限
        this.amount = Math.min(Amount, MAX_STACKS);

        // 添加一大一小两张能力图
        String path128 = "RaisePigResources/img/powers/Example84.png";
        String path48 = "RaisePigResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }
    
    // 重写stackPower方法，限制叠加时不超过上限
    @Override
    public void stackPower(int stackAmount) {
        this.amount = Math.min(this.amount + stackAmount, MAX_STACKS);
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount*10); // 这样，%d就被替换成能力的层数
    }

    // 获得能量时，有能力层数*10的概率增加1点最大能量值
    public void atEnergyGain() {
//        // 非荆棘伤害，非生命流失伤害，伤害来源不为空，伤害来源不是能力持有者本身，伤害大于0
//        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && damageAmount > 0) {
//            // 能力闪烁一下
//            this.flash();
//
//            // 添加回复action
//            this.addToTop(new HealAction(owner, owner, this.amount));
//        }
//
//        // 如果该能力不会修改受到伤害的数值，按原样返回即可
//        return damageAmount;
        int random = AbstractDungeon.cardRandomRng.random(10)+1;
        if(random <= this.amount){
            this.flash();
            AbstractDungeon.actionManager.addToTop(
                    new IncreaseMaxEnergyAction(1)
            );
        }
    }
}

