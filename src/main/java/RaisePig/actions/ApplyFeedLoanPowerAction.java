package RaisePig.actions;

import RaisePig.powers.FeedLoanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ApplyFeedLoanPowerAction extends AbstractGameAction {
    private int amount;
    private int energyLoss;

    public ApplyFeedLoanPowerAction(AbstractCreature target, int amount, int energyLoss) {
        this.target = target;
        this.amount = amount;
        this.energyLoss = energyLoss;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (target.hasPower(FeedLoanPower.POWER_ID)) {
            // 如果已存在，累加层数和能量损失
            FeedLoanPower existingPower = (FeedLoanPower) target.getPower(FeedLoanPower.POWER_ID);
            existingPower.amount += this.amount;
            existingPower.energyLoss += this.energyLoss;
            existingPower.updateDescription();
            existingPower.flash();
        } else {
            // 如果不存在，创建新的
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                            target,
                            target,
                            new FeedLoanPower(target, this.amount, this.energyLoss)
                    )
            );
        }
        this.isDone = true;
    }
}
