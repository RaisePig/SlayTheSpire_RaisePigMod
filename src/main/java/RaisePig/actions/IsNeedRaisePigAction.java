package RaisePig.actions;

import RaisePig.Helper.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

//action不需要注册，在卡牌的use方法中直接new即可。
public class IsNeedRaisePigAction extends AbstractGameAction {
    public IsNeedRaisePigAction(AbstractPlayer target) {
        this.target = target;
    }

    @Override
    public void update() {
        //如果有养猪能力，则判定是否触发养猪能力，触发则增加1点最大能量值
        if(target.hasPower(ModHelper.makePath("RaisePigPower"))){
            AbstractPower power = target.getPower(ModHelper.makePath("RaisePigPower"));
            int number = power.amount;
            int random = AbstractDungeon.cardRandomRng.random(1, 10);
            if(random <= number){
                power.flash();
                // 增加1点最大能量值
                AbstractDungeon.actionManager.addToBottom(
                        new IncreaseMaxEnergyAction(1));
            }
        }
        this.isDone = true;//isDone=true是必须写的，这表示你这个action执行完了。
    }

}

