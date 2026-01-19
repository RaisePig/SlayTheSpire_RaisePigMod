package RaisePig.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

//action不需要注册，在卡牌的use方法中直接new即可。
public class IncreaseMaxEnergyAction extends AbstractGameAction {
    public IncreaseMaxEnergyAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractDungeon.player.energy.energy+=amount;
        this.isDone = true;//isDone=true是必须写的，这表示你这个action执行完了。
    }

}

