package com.zalexdev.stryker.appintro.install;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zalexdev.stryker.R;
import com.zalexdev.stryker.mux.MuxColors;

import java.util.ArrayList;
import java.util.List;

public final class LogAdapter extends RecyclerView.Adapter<LogAdapter.VH> {

    private static final int MAX_LINES = 600;

    private final List<LogLine> lines = new ArrayList<>();
    private final Context context;

    public LogAdapter(Context context) {
        this.context = context;
    }

    public void append(LogLine line) {
        String group = progressGroup(line.text);
        if (group != null && !lines.isEmpty()) {
            int lastIdx = lines.size() - 1;
            LogLine last = lines.get(lastIdx);
            if (last.level == line.level && group.equals(progressGroup(last.text))) {
                lines.set(lastIdx, line);
                notifyItemChanged(lastIdx);
                return;
            }
        }
        lines.add(line);
        notifyItemInserted(lines.size() - 1);
        while (lines.size() > MAX_LINES) {
            lines.remove(0);
            notifyItemRemoved(0);
        }
    }

    private static String progressGroup(String text) {
        if (text == null) return null;
        if (text.contains("Receiving objects")) return "recv";
        if (text.contains("Resolving deltas")) return "delta";
        if (text.contains("Counting objects")) return "count";
        if (text.contains("Compressing objects")) return "compress";
        if (text.contains("Updating files")) return "updating";
        if (text.contains("Unpacking objects")) return "unpack";
        return null;
    }

    public void clear() {
        int size = lines.size();
        lines.clear();
        if (size > 0) notifyItemRangeRemoved(0, size);
    }

    public String asText() {
        StringBuilder sb = new StringBuilder();
        for (LogLine line : lines) {
            sb.append('[').append(line.level.name()).append("] ").append(line.text).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public int size() {
        return lines.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.install_log_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        LogLine line = lines.get(position);
        MuxColors mux = MuxColors.resolve(context);

        holder.chip.setText(chipFor(line.level));
        holder.chip.setTextColor(colorFor(line.level, mux));

        holder.text.setText(stripPrefix(line.text));
        holder.text.setTextColor(line.level == LogLevel.ERROR ? mux.danger : mux.text);
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    private int colorFor(LogLevel level, MuxColors mux) {
        switch (level) {
            case SUCCESS:
                return ContextCompat.getColor(context, R.color.mux_success);
            case WARN:
            case STEP:
                return mux.accent;
            case ERROR:
                return mux.danger;
            case CMD:
                return mux.primary;
            case INFO:
            default:
                return mux.dim;
        }
    }

    private static String chipFor(LogLevel level) {
        switch (level) {
            case SUCCESS: return "OK";
            case WARN:    return "WRN";
            case ERROR:   return "ERR";
            case CMD:     return "CMD";
            case STEP:    return ">>>";
            case INFO:
            default:      return "...";
        }
    }

    private static String stripPrefix(String text) {
        if (text == null) return "";
        return text.replace("[STRYKER]", "").replace("[STRYKER:warn]", "").trim();
    }

    static final class VH extends RecyclerView.ViewHolder {
        final TextView chip;
        final TextView text;

        VH(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.log_chip);
            text = itemView.findViewById(R.id.log_text);
        }
    }
}
