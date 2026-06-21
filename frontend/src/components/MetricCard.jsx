export default function MetricCard({ label, value, tone = 'neutral', icon: Icon }) {
  return (
    <article className={`metric metric-${tone}`}>
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
      </div>
      {Icon && <Icon size={24} />}
    </article>
  );
}
