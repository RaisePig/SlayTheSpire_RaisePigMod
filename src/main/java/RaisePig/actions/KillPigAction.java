package RaisePig.actions;

import RaisePig.Helper.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

//action不需要注册，在卡牌的use方法中直接new即可。
public class KillPigAction extends AbstractGameAction {
    public KillPigAction(AbstractCreature target) {
        this.target = target;
    }

    @Override
    public void update() {
        // 如果目标身上有投喂状态
        if (target.hasPower(ModHelper.makePath("FeedPower"))){
            // 记录投喂状态层数
            int feed = target.getPower(ModHelper.makePath("FeedPower")).amount;
            // 移除所有状态
            target.getPower(ModHelper.makePath("FeedPower")).flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveAllPowersAction(target,false)
            );
            // 造成投喂层数的伤害
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(
                            target,
                            new DamageInfo(
                                    AbstractDungeon.player,
                                    feed,
                                    DamageInfo.DamageType.NORMAL
                            )
                    )
            );
            // 失去投喂状态层数对应的能量
            AbstractDungeon.actionManager.addToBottom(
                    new LoseEnergyAction(feed)
            );
        }
        this.isDone = true;//isDone=true是必须写的，这表示你这个action执行完了。
    }

}

