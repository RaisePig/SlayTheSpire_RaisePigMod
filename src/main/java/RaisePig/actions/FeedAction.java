package RaisePig.actions;

import RaisePig.powers.FatteningPower;
import RaisePig.powers.FeedPower;
import RaisePig.powers.LoseHPPower;
import RaisePig.powers.LoseMaxEnergyPower;
import RaisePig.powers.PrecisionFarmingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 统一投喂入口：
 * 1) 若玩家拥有 LoseMaxEnergy 状态，则先失去对应层数能量上限（一次性结算并移除该状态）
 * 2) 若玩家拥有 LoseHP 状态，则先失去对应层数生命（一次性结算并移除该状态）
 * 3) 再对目标施加 FeedPower（并在此处统一处理“精细化养殖/催肥”等全局投喂修正）
 */
public class FeedAction extends AbstractGameAction {
    private final AbstractCreature source;
    private final AbstractCreature target;
    private final int baseFeed;

    public FeedAction(AbstractCreature target, AbstractCreature source, int baseFeed) {
        this.target = target;
        this.source = source;
        this.baseFeed = baseFeed;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

        // ==== 2) 计算最终投喂量（全局修正） ====
        int feedAmount = Math.max(0, this.baseFeed);

        // 精细化养殖：投喂额外 +X
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(PrecisionFarmingPower.POWER_ID)) {
            feedAmount += AbstractDungeon.player.getPower(PrecisionFarmingPower.POWER_ID).amount;
        }

        // 催肥：下一次投喂数量翻倍（并消耗一次次数）
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(FatteningPower.POWER_ID)) {
            feedAmount *= 2;
            addToTop(
                    new ApplyPowerAction(source, source, new FatteningPower(source, -1), -1));
        }

        // ==== 3) 应用投喂 ====
        if (feedAmount > 0) {
            addToTop(
                    new ApplyPowerAction(target, source,
                            new FeedPower(target, feedAmount)));
        }

        this.isDone = true;
    }
}
