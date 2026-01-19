package RaisePig;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
//action不需要注册，在卡牌的use方法中直接new即可。
public class ExampleAction extends AbstractGameAction {
    // 伤害信息
    public DamageInfo info;

    public ExampleAction(AbstractMonster target, DamageInfo info) {
        this.target = target;
        this.info = info;
    }

    @Override
    public void update() {
        // 目标受到伤害
        this.target.damage(this.info);
        // 如果目标死亡，则抽一张牌
        if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead
                && !this.target.hasPower("Minion")) {
            this.addToTop(new DrawCardAction(1));
        }
        this.isDone = true;//isDone=true是必须写的，这表示你这个action执行完了。
    }

}

